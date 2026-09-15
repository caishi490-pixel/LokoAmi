<script lang="ts">
    import Tab from "../../common/modal/Tab.svelte";
    import ButtonSetting from "../../common/setting/ButtonSetting.svelte";
    import {addMicrosoftAccount, addMicrosoftAccountCopyUrl} from "../../../../integration/rest.js";

    let loading = false;

    async function addAccount() {
        loading = true;
        await addMicrosoftAccount();
    }

    async function copyLoginUrl() {
        await addMicrosoftAccountCopyUrl();
    }
</script>

<Tab>
    <ButtonSetting title="关联账号" on:click={addAccount} {loading}/>
    <ButtonSetting title="复制链接" secondary={true} on:click={copyLoginUrl} inset={true}/>
</Tab>