<script lang="ts">
    import {fade, fly} from "svelte/transition";
    import {createEventDispatcher} from "svelte";
    import {backIn, backOut} from "svelte/easing";

    export let title: string;
    export let icon: string;
    export let index: number;

    let hovered = false;

    const dispatch = createEventDispatcher();
</script>

<!-- svelte-ignore a11y-no-static-element-interactions -->
<!-- svelte-ignore a11y-click-events-have-key-events -->
<div class="main-button" on:mouseenter={() => hovered = true} on:mouseleave={() => hovered = false} on:click={() => hovered = false}
     on:click={() => dispatch("click")} out:fly|global={{duration: 400, x: -500, delay: index * 100, easing: backIn}}
     in:fly|global={{duration: 400, x: -500, delay: index * 100, easing: backOut}}>
    <div class="icon">
        {#if !hovered}
            <img transition:fade={{duration: 200}} src="img/menu/icon-{icon}.svg" alt={icon}>
        {:else}
            <img transition:fade={{duration: 200}} src="img/menu/icon-{icon}-hover.svg" alt={icon}>
        {/if}
    </div>

    <div class="title">{title}</div>

    <div class="wrapped-content">
        <slot parentHovered={hovered}/>
    </div>
</div>

<style lang="scss">
  @use "../../../../colors.scss" as *;

  /* Esports：0 圆角 + 右上角切角 + 左侧状态条；辉光只出现在悬停态 */
  .main-button {
    width: 590px;
    padding: 18px 28px;
    display: grid;
    grid-template-columns: max-content 1fr max-content;
    align-items: center;
    column-gap: 22px;
    cursor: pointer;
    border-radius: 0;

    background-color: rgba($panel-color, 0.72);
    border: 1px solid $line-color;
    border-left: 3px solid $line-color-strong;
    clip-path: polygon(0 0, calc(100% - 14px) 0, 100% 14px, 100% 100%, 0 100%);

    transition: background-color .16s ease, border-color .16s ease, box-shadow .16s ease;
    will-change: background-color;

    &:hover {
      background-color: $neon-faint;
      border-color: $neon-dim;
      border-left-color: $neon-color;
      box-shadow: 0 0 24px $neon-glow-sm;

      .icon {
        border-color: $neon-color;
        box-shadow: 0 0 14px $neon-glow-sm;
      }

      .title {
        color: $neon-color;
      }
    }
  }

  /* 图标位由圆形改为切角方块，保持硬边 */
  .icon {
    width: 62px;
    height: 62px;
    border: 1px solid $line-color;
    background-color: $void-color;
    border-radius: 0;
    clip-path: polygon(0 0, calc(100% - 10px) 0, 100% 10px, 100% 100%, 0 100%);
    display: grid;
    place-items: center;
    transition: border-color .16s ease, box-shadow .16s ease;

    img {
      width: 34px;
      height: 34px;
    }
  }

  /* 中文会回退到系统字体（Rajdhani 无中文字形），字号与字距按中文调过 */
  .title {
    font-family: var(--font-display);
    font-size: 23px;
    font-weight: 700;
    letter-spacing: 2px;
    color: $menu-text-color;
    transition: color .16s ease;
  }
</style>
