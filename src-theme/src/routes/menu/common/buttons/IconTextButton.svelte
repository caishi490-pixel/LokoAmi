<script lang="ts">
    import {createEventDispatcher} from "svelte";

    export let title: string;
    export let icon: string;
    export let disabled = false;
    /*
     * appearance:
     *   "default" —— 全站现有外观（玫粉图标块 + 悬停滑入）
     *   "v3"      —— 启动页设计稿 v3 的底栏按钮（圆角面板 + 天蓝悬停）
     */
    export let appearance: "default" | "v3" = "default";

    const dispatch = createEventDispatcher();
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<!-- svelte-ignore a11y-no-static-element-interactions -->
<button class="icon-text-button" class:v3={appearance === "v3"} on:click={() => dispatch("click")} {disabled}>
    <div class="icon">
        <img src="img/menu/{icon}" alt={title}>
    </div>
    <div class="title">{title}</div>
</button>

<style lang="scss">
    @use "../../../../colors.scss" as *;

    .icon-text-button {
      display: flex;
      border: none;

      border-radius: 0;
      align-items: center;
      overflow: hidden;
      background: linear-gradient(to left, rgba($panel-color, .36) 50%, $neon-color 50%);
      background-size: 200% 100%;
      background-position: right bottom;
      will-change: background-position;
      transition: ease opacity .2s, background-position .2s ease-out;

      &:not([disabled]):hover {
        &:hover {
          background-position: left bottom;
          cursor: pointer;
        }
      }

      &[disabled] {
        opacity: .6;
      }
    }

    .icon {
      height: 58px;
      width: 58px;
      background-color: $neon-color;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .title {
      font-family: var(--font-display);
      font-size: 20px;
      font-weight: 700;
      letter-spacing: 1px;
      color: $menu-text-color;
      padding: 0 30px;
    }

    /* ---------- 设计稿 v3 ---------- */
    .icon-text-button.v3 {
      gap: 8px;
      padding: 10px 18px;
      border-radius: 8px;
      background: rgba(18, 34, 54, .82);
      border: 1px solid rgba(143, 166, 196, .14);
      transition: background-color .2s, border-color .2s, color .2s;

      &:not([disabled]):hover {
        background-position: right bottom;
        background-color: #152840;
        border-color: rgba(56, 189, 248, .5);
      }

      .icon {
        height: 16px;
        width: 16px;
        background-color: transparent;
      }

      .title {
        padding: 0;
        font-size: 11px;
        font-weight: 600;
        letter-spacing: 1.2px;
        text-transform: uppercase;
        color: #8FA6C4;
        transition: color .2s;
      }

      &:not([disabled]):hover .title {
        color: #38BDF8;
      }
    }
</style>
