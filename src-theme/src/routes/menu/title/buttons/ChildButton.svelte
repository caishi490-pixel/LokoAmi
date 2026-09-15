<script lang="ts">
    import {fade} from "svelte/transition";
    import {createEventDispatcher} from "svelte";
    import ToolTip from "../../common/ToolTip.svelte";

    export let title: string;
    export let icon: string;
    export let parentHovered: boolean;

    const dispatch = createEventDispatcher();
</script>

<!-- svelte-ignore a11y-no-static-element-interactions -->
<!-- svelte-ignore a11y-click-events-have-key-events -->
<div class="child-button" on:click|stopPropagation={() => dispatch("click")} class:parent-hovered={parentHovered}>
    <ToolTip color="black" text="加入 Realms 服务器"/>

    <div class="icon">
        {#if parentHovered}
            <img transition:fade="{{ duration: 200 }}" src="img/menu/icon-{icon}-hover.svg" alt={title}>
        {:else}
            <img transition:fade="{{ duration: 200 }}" src="img/menu/icon-{icon}.svg" alt={title}>
        {/if}
    </div>

    <div class="title">{title}</div>
</div>

<style lang="scss">
    /* 启动页设计稿 v3：小圆角胶囊，随父按钮悬停点亮 */
    .child-button {
      position: relative;
      display: flex;
      align-items: center;
      padding: 8px 14px;
      border-radius: 7px;

      background-color: rgba(10, 22, 40, .82);
      border: 1px solid rgba(143, 166, 196, .14);
      transition: background-color .2s, border-color .2s, box-shadow .2s;

      &.parent-hovered {
        background-color: rgba(56, 189, 248, .12);
        border-color: rgba(56, 189, 248, .5);

        .title {
          color: #38BDF8;
        }
      }
    }

    .title {
      font-family: var(--font-display);
      font-size: 13px;
      font-weight: 700;
      letter-spacing: 1.5px;
      color: #EAF2FF;
      transition: color .2s;
      margin-left: 10px;
    }

    .icon { /* necessary because svelte's transition system sucks */
      width: 22px;
      height: 22px;
      position: relative;

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 22px;
        height: 22px;
      }
    }
</style>
