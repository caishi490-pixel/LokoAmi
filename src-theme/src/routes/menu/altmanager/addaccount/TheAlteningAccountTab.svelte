<script lang="ts">
    import Tab from "../../common/modal/Tab.svelte";
    import IconTextInput from "../../common/setting/IconTextInput.svelte";
    import ButtonSetting from "../../common/setting/ButtonSetting.svelte";
    import {addAlteningAccount, browse} from "../../../../integration/rest";

    let token = "";
    let loading = false;
    $: disabled = validateToken(token);

    function validateToken(token: string) {
        return token.length === 0;
    }

    async function addAccount() {
        if (disabled) {
            return;
        }
        loading = true;
        await addAlteningAccount(token);
    }
</script>

<Tab>
    <IconTextInput icon="user" title="令牌" bind:value={token}/>
    <ButtonSetting {disabled} title="添加账号" on:click={addAccount} listenForEnter={true} inset={true} {loading}/>
    <ButtonSetting title="获取账号令牌" on:click={() => browse("THE_ALTENING")} secondary={true}/>
</Tab>
