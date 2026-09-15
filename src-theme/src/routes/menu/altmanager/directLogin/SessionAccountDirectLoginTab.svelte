<script lang="ts">
    import Tab from "../../common/modal/Tab.svelte";
    import IconTextInput from "../../common/setting/IconTextInput.svelte";
    import ButtonSetting from "../../common/setting/ButtonSetting.svelte";
    import {directLoginToSessionAccount} from "../../../../integration/rest";

    let token = "";
    $: disabled = validateSessionId(token);

    function validateSessionId(token: string): boolean {
        return token.length === 0;
    }

    async function login() {
        if (disabled) {
            return;
        }
        await directLoginToSessionAccount(token);
    }
</script>

<Tab>
    <IconTextInput icon="user" title="会话 ID" bind:value={token}/>
    <ButtonSetting title="登录" {disabled} on:click={login} listenForEnter={true} inset={true} />
</Tab>