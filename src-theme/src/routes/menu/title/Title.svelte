<script lang="ts">
    import MainButton from "./buttons/MainButton.svelte";
    import ChildButton from "./buttons/ChildButton.svelte";
    import ConfettiBackground from "./ConfettiBackground.svelte";
    import ButtonContainer from "../common/buttons/ButtonContainer.svelte";
    import IconTextButton from "../common/buttons/IconTextButton.svelte";
    import IconButton from "../common/buttons/IconButton.svelte";
    import {
        browse,
        exitClient,
        getClientUpdate,
        openScreen,
        toggleBackgroundShaderEnabled
    } from "../../../integration/rest";
    import Menu from "../common/Menu.svelte";
    import {fly} from "svelte/transition";
    import {onMount} from "svelte";
    import {notification} from "../common/header/notification_store";
    import {isAnniversary} from "../../../util/utils";

    let regularButtonsShown = true;
    let clientButtonsShown = false;

    onMount(() => {
        setTimeout(async () => {
            const clientUpdate = await getClientUpdate();

            if (clientUpdate.update) {
                notification.set({
                    title: `LokoAmi ${clientUpdate.update.clientVersion} 已发布！`,
                    message: `请前往官网下载最新版本。`,
                    error: false,
                    delay: 99999999
                });
            }
        }, 2000);
    });

    function toggleButtons() {
        if (clientButtonsShown) {
            clientButtonsShown = false;
            setTimeout(() => {
                regularButtonsShown = true;
            }, 750);
        } else {
            regularButtonsShown = false;
            setTimeout(() => {
                clientButtonsShown = true;
            }, 750);
        }
    }
</script>

<div class="title-screen">
    {#if isAnniversary()}
        <ConfettiBackground />
    {/if}

    <Menu>
        <div class="content">
            <div class="main-buttons">
                {#if regularButtonsShown}
                    <MainButton title="单人游戏" icon="singleplayer" index={0}
                                on:click={() => openScreen("singleplayer")}/>

                    <MainButton title="多人游戏" icon="multiplayer" let:parentHovered
                                on:click={() => openScreen("multiplayer")} index={1}>
                        <ChildButton title="Realms" icon="realms" {parentHovered}
                                     on:click={() => openScreen("multiplayer_realms")}/>
                    </MainButton>
                    <MainButton title="LokoAmi" icon="liquidbounce" on:click={toggleButtons} index={2}/>
                    <MainButton title="选项" icon="options" on:click={() => openScreen("options")} index={3}/>
                {:else if clientButtonsShown}
                    <MainButton title="代理管理" icon="proxymanager" on:click={() => openScreen("proxymanager")}
                                index={0}/>
                    <MainButton title="界面设置" icon="clickgui" on:click={() => openScreen("clickgui")} index={1}/>
                    <!-- <MainButton title="脚本" icon="scripts" index={2}/> -->
                    <MainButton title="返回" icon="back-large" on:click={toggleButtons} index={2}/>
                {/if}
            </div>

            <div class="additional-buttons" transition:fly|global={{duration: 700, y: 100}}>
                <ButtonContainer>
                    <IconTextButton icon="icon-exit.svg" title="退出游戏" on:click={exitClient}/>
                    <IconTextButton icon="icon-change-background.svg" title="切换光影"
                                    on:click={toggleBackgroundShaderEnabled}/>
                </ButtonContainer>
            </div>

            <div class="social-buttons" transition:fly|global={{duration: 700, y: 100}}>
                <ButtonContainer>
                    <IconButton title="论坛" icon="nodebb" on:click={() => browse("MAINTAINER_FORUM")}/>
                    <IconButton title="GitHub" icon="github" on:click={() => browse("MAINTAINER_GITHUB")}/>
                    <IconButton title="Discord" icon="discord" on:click={() => browse("MAINTAINER_DISCORD")}/>
                    <IconButton title="Twitter" icon="twitter" on:click={() => browse("MAINTAINER_TWITTER")}/>
                    <IconButton title="YouTube" icon="youtube" on:click={() => browse("MAINTAINER_YOUTUBE")}/>
                    <IconTextButton title="liquidbounce.net" icon="icon-liquidbounce.net.svg"
                                    on:click={() => browse("CLIENT_WEBSITE")}/>
                </ButtonContainer>
            </div>
        </div>
    </Menu>
</div>

<style>
    .title-screen {
        position: relative;
        isolation: isolate;
    }

    .content {
        flex: 1;
        display: grid;
        grid-template-areas:
            "a ."
            "b c";
        grid-template-rows: 1fr max-content;
        grid-template-columns: 1fr max-content;
    }

    .main-buttons {
        display: flex;
        flex-direction: column;
        row-gap: 25px;
        grid-area: a;
    }

    .additional-buttons {
        grid-area: b;
    }

    .social-buttons {
        grid-area: c;
    }
</style>
