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
    <ToolTip color="black" text="加入 Realms 服务器" />

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
    @use "../../../../colors.scss" as *;

    /* Esports：0 圆角 + 平行四边形裁切 */
    .child-button {
      position: relative;
      display: flex;
      align-items: center;
      border-radius: 0;
      clip-path: polygon(7px 0, 100% 0, calc(100% - 7px) 100%, 0 100%);
      padding: 13px 18px;

      background-color: rgba($void-color, 0.82);
      border: 1px solid $line-color;
      transition: background-color .16s ease, border-color .16s ease, box-shadow .16s ease;

      &.parent-hovered {
        background-color: $neon-faint;
        border-color: $neon-color;
        box-shadow: 0 0 16px $neon-glow-sm;

        .title {
          color: $neon-color;
        }
      }
    }

    .title {
      font-family: var(--font-display);
      font-size: 15px;
      font-weight: 700;
      letter-spacing: 2px;
      color: $menu-text-color;
      transition: color .16s ease;
      margin-left: 12px;
    }

    .icon { /* necessary because svelte's transition system sucks */
      width: 26px;
      height: 26px;
      position: relative;

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
    }
</style>
