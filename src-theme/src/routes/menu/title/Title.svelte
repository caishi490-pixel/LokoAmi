<script lang="ts">
    import MainButton from "./buttons/MainButton.svelte";
    import ChildButton from "./buttons/ChildButton.svelte";
    import ConfettiBackground from "./ConfettiBackground.svelte";
    import SakuraBackground from "./SakuraBackground.svelte";
    import ButtonContainer from "../common/buttons/ButtonContainer.svelte";
    import IconTextButton from "../common/buttons/IconTextButton.svelte";
    import IconButton from "../common/buttons/IconButton.svelte";
    import {
        browse,
        exitClient,
        getClientInfo,
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

    /* 设计稿 v3 的 eyebrow 与贡献清单都需要版本信息，实时取 */
    let clientVersion = "";
    let gameVersion = "";

    onMount(() => {
        getClientInfo()
            .then((info) => {
                clientVersion = info.clientVersion;
                gameVersion = info.gameVersion;
            })
            .catch(() => {
                /* 取不到就留空，相关行由模板判断是否渲染 */
            });

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
    <SakuraBackground/>

    {#if isAnniversary()}
        <ConfettiBackground/>
    {/if}

    <Menu>
        <div class="content">
            <main class="hero">
                <div class="eyebrow">
                    Minecraft Client{#if gameVersion} · {gameVersion}{/if}
                </div>

                <h1>Loko<em>Ami</em></h1>

                <div class="credits">
                    <div class="ct">贡献清单 / Credits</div>
                    <div class="row">
                        <span class="k">开发</span>
                        <span class="v hot">lie_fish</span>
                    </div>
                    {#if clientVersion}
                        <div class="row">
                            <span class="k">版本号</span>
                            <span class="v">V{clientVersion}</span>
                        </div>
                    {/if}
                </div>

                <nav class="main-buttons">
                    {#if regularButtonsShown}
                        <MainButton title="单人游戏" icon="singleplayer" index={0}
                                    on:click={() => openScreen("singleplayer")}/>

                        <MainButton title="多人游戏" icon="multiplayer" let:parentHovered
                                    on:click={() => openScreen("multiplayer")} index={1}>
                            <ChildButton title="Realms" icon="realms" {parentHovered}
                                         on:click={() => openScreen("multiplayer_realms")}/>
                        </MainButton>
                        <MainButton title="LokoAmi" icon="liquidbounce" primary on:click={toggleButtons} index={2}/>
                        <MainButton title="选项" icon="options" on:click={() => openScreen("options")} index={3}/>
                    {:else if clientButtonsShown}
                        <MainButton title="代理管理" icon="proxymanager" on:click={() => openScreen("proxymanager")}
                                    index={0}/>
                        <MainButton title="界面设置" icon="clickgui" on:click={() => openScreen("clickgui")} index={1}/>
                        <!-- <MainButton title="脚本" icon="scripts" index={2}/> -->
                        <MainButton title="返回" icon="back-large" on:click={toggleButtons} index={2}/>
                    {/if}
                </nav>
            </main>

            <footer class="foot">
                <div class="additional-buttons" transition:fly|global={{duration: 700, y: 100}}>
                    <ButtonContainer appearance="v3">
                        <IconTextButton appearance="v3" icon="icon-exit.svg" title="退出游戏" on:click={exitClient}/>
                        <IconTextButton appearance="v3" icon="icon-change-background.svg" title="切换光影"
                                        on:click={toggleBackgroundShaderEnabled}/>
                    </ButtonContainer>
                </div>

                <div class="social-buttons" transition:fly|global={{duration: 700, y: 100}}>
                    <ButtonContainer appearance="v3">
                        <IconButton appearance="v3" title="论坛" icon="nodebb"
                                    on:click={() => browse("MAINTAINER_FORUM")}/>
                        <IconButton appearance="v3" title="GitHub" icon="github"
                                    on:click={() => browse("MAINTAINER_GITHUB")}/>
                        <IconButton appearance="v3" title="Discord" icon="discord"
                                    on:click={() => browse("MAINTAINER_DISCORD")}/>
                        <IconButton appearance="v3" title="Twitter" icon="twitter"
                                    on:click={() => browse("MAINTAINER_TWITTER")}/>
                        <IconButton appearance="v3" title="YouTube" icon="youtube"
                                    on:click={() => browse("MAINTAINER_YOUTUBE")}/>
                        <IconTextButton appearance="v3" title="lokoami.net" icon="icon-liquidbounce.net.svg"
                                        on:click={() => browse("CLIENT_WEBSITE")}/>
                    </ButtonContainer>
                </div>
            </footer>
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
        display: flex;
        flex-direction: column;
        min-height: 0;
    }

    /* 设计稿 v3：主区垂直居中，底栏贴底 */
    .hero {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        min-height: 0;
    }

    .eyebrow {
        display: flex;
        align-items: center;
        gap: 11px;
        margin-bottom: 16px;
        font-family: var(--font-display);
        font-size: 11px;
        font-weight: 700;
        letter-spacing: 3.4px;
        text-transform: uppercase;
        color: #F0508C;
    }

    /* eyebrow 右侧那条渐隐细线 */
    .eyebrow::after {
        content: '';
        height: 1px;
        width: 76px;
        background: linear-gradient(90deg, #F0508C, transparent);
    }

    h1 {
        margin-bottom: 26px;
        font-family: var(--font-display);
        font-size: clamp(56px, 7.6vw, 104px);
        font-weight: 800;
        line-height: .98;
        letter-spacing: -1px;
        color: #EAF2FF;
    }

    h1 em {
        font-style: normal;
        background: linear-gradient(96deg, #F0508C 0%, #FF8FB8 55%, #38BDF8 100%);
        -webkit-background-clip: text;
        background-clip: text;
        color: transparent;
    }

    /* 贡献清单卡片（取代原先写在顶栏 logo 旁的署名） */
    .credits {
        position: relative;
        display: inline-flex;
        flex-direction: column;
        align-self: flex-start;
        min-width: 300px;
        margin-bottom: 40px;
        padding: 4px 22px;
        overflow: hidden;

        background-color: rgba(18, 34, 54, .82);
        border: 1px solid rgba(143, 166, 196, .14);
        border-radius: 10px;
    }

    .credits::before {
        content: '';
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        width: 4px;
        background: #F0508C;
    }

    .ct {
        padding: 14px 0 4px;
        font-family: var(--font-display);
        font-size: 10px;
        font-weight: 700;
        letter-spacing: 2.6px;
        text-transform: uppercase;
        color: #F0508C;
    }

    .row {
        display: flex;
        align-items: baseline;
        gap: 16px;
        padding: 9px 0;
        border-top: 1px solid rgba(143, 166, 196, .09);
    }

    .row:first-of-type {
        border-top: none;
    }

    .k {
        min-width: 60px;
        font-size: 11px;
        font-weight: 700;
        letter-spacing: 1.6px;
        text-transform: uppercase;
        color: #8FA6C4;
    }

    .v {
        font-size: 16px;
        font-weight: 700;
        letter-spacing: .5px;
        color: #EAF2FF;
    }

    .v.hot {
        color: #F0508C;
    }

    .main-buttons {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        row-gap: 11px;
    }

    /* 设计稿 v3：底栏左右分列 */
    .foot {
        display: flex;
        align-items: flex-end;
        justify-content: space-between;
        gap: 24px;
        padding-top: 24px;
    }
</style>
