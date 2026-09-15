<script lang="ts">
    import type {Snippet} from "svelte";
    import {fade} from "svelte/transition";
    import {quintOut} from "svelte/easing";

    let { title, icon, children } = $props<{
        title: string;
        icon?: string;
        children: Snippet;
    }>();
</script>

<div class="window" transition:fade|global={{duration: 200, easing: quintOut}}>
    <div class="title">
        {#if icon}
            <img
                    class="icon"
                    src="img/clickgui/icon-{icon}.svg"
                    alt="icon"
            />
        {/if}
        <span class="title-text">{title}</span>
    </div>
    <div class="content">
        {@render children()}
    </div>
</div>

<style lang="scss">
  @use "../../../colors.scss" as *;

  /* Esports：0 圆角 + 右上切角 + 霓虹描边与辉光 */
  .window {
    position: fixed;
    top: 70px;
    left: 50%;
    transform: translateX(-50%);
    width: min(820px, 92vw);
    --window-max-height: 70vh;
    background-color: rgba($panel-color, 0.92);
    max-height: var(--window-max-height, none);
    border: 1px solid $neon-dim;
    clip-path: polygon(0 0, calc(100% - 14px) 0, 100% 14px, 100% 100%, 0 100%);
    overflow: hidden;
    box-shadow: 0 0 28px $neon-glow-sm;
    user-select: none;
  }

  .title {
    display: grid;
    grid-template-columns: max-content 1fr;
    align-items: center;
    column-gap: 12px;
    background-color: rgba($panel-color, 0.96);
    padding: 16px 22px;
    font-family: var(--font-display);
    font-size: 16px;
    font-weight: 700;
    letter-spacing: 1px;
    color: $clickgui-text-color;
    border-bottom: 2px solid $neon-color;
  }

  .title-text {
    font-weight: 700;
  }

  .content {
    padding: 12px 22px 18px;
    overflow: auto;
    max-height: calc(var(--window-max-height, 9999px) - 60px);
  }

  /* Esports：容器描边（补完注入） */
  .window, .content {
    border: 1px solid $neon-dim;
  }

  /* Esports-FONT-INJECT */
  .title {
    font-family: var(--font-display);
  }
  /* Esports-BOX-INJECT */
  .window, .content {
    border: 1px solid $neon-dim;
  }
</style>
