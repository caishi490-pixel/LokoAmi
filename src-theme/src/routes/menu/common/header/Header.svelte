<script lang="ts">
    import Account from "./account/Account.svelte";
    import AnimatedLogo from "./AnimatedLogo.svelte";
    import Notifications from "./Notifications.svelte";
    import {listen} from "../../../../integration/ws";
    import {getModules} from "../../../../integration/rest";
    import {onMount} from "svelte";
    import type {
        AccountManagerAdditionEvent,
        AccountManagerLoginEvent,
        AccountManagerMessageEvent
    } from "../../../../integration/events";
    import {notification} from "./notification_store";

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

    /* 设计稿 v3 顶栏右侧的「就绪」状态徽章：模块数实时取，不写死 */
    let moduleCount: number | null = null;

    onMount(async () => {
        try {
            moduleCount = (await getModules()).length;
        } catch {
            moduleCount = null;
        }
    });
</script>

<div class="header">
    <AnimatedLogo/>

    <Notifications/>

    <div class="right">
        {#if moduleCount !== null}
            <div class="stat">
                <span class="dot"></span>
                就绪 · {moduleCount} 个模块
            </div>
        {/if}

        <Account/>
    </div>
</div>

<style lang="scss">
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 24px;
    margin-bottom: 40px;
  }

  .right {
    display: flex;
    align-items: center;
    gap: 18px;
  }

  /* 设计稿 v3 的 .stat */
  .stat {
    display: flex;
    align-items: center;
    gap: 9px;
    font-family: var(--font-display);
    font-size: 11px;
    font-weight: 600;
    letter-spacing: 1.2px;
    color: #8FA6C4;
    white-space: nowrap;
  }

  .dot {
    width: 7px;
    height: 7px;
    border-radius: 50%;
    background: #4ADE80;
    box-shadow: 0 0 10px #4ADE80;
    animation: stat-pulse 2.4s ease-in-out infinite;
  }

  @keyframes stat-pulse {
    0%, 100% {
      opacity: 1;
    }

    50% {
      opacity: .32;
    }
  }
</style>
