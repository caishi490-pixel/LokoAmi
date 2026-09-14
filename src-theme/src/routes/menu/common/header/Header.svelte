<script lang="ts">
    import Account from "./account/Account.svelte";
    import AnimatedLogo from "./AnimatedLogo.svelte";
    import Notifications from "./Notifications.svelte";
    import {listen} from "../../../../integration/ws";
    import {location} from "svelte-spa-router";
    import type {
        AccountManagerAdditionEvent,
        AccountManagerLoginEvent,
        AccountManagerMessageEvent
    } from "../../../../integration/events";
    import {notification} from "./notification_store";
    import {isAnniversary} from "../../../../util/utils";

    listen("accountManagerAddition", (e: AccountManagerAdditionEvent) => {
        if (!e.error) {
            notification.set({
                title: "账号管理",
                message: `已成功添加账号 ${e.username}`,
                error: false
            });
        } else {
            notification.set({
                title: "账号管理",
                message: e.error,
                error: true
            });
        }
    });

    listen("accountManagerMessage", (e: AccountManagerMessageEvent) => {
        notification.set({
            title: "账号管理",
            message: e.message,
            error: false
        });
    });

    listen("accountManagerLogin", (e: AccountManagerLoginEvent) => {
        if (!e.error) {
            notification.set({
                title: "账号管理",
                message: `已成功登录账号 ${e.username}`,
                error: false
            });
        } else {
            notification.set({
                title: "账号管理",
                message: e.error,
                error: true
            });
        }
    });
</script>

<div class="header">
    {#if $location === "/title" && isAnniversary()}
        <AnimatedLogo/>
    {:else}
        <img class="logo" src="img/lb-logo.svg" alt="LokoAmi logo">
    {/if}

    <Notifications />

    <Account/>
</div>

<style lang="scss">
  .header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 60px;
    align-items: center;
  }

  .logo {
    display: block;
    width: 261.263px;
    height: 98px;
    flex: 0 0 auto;
  }
</style>
