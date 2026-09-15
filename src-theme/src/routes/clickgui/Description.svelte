<script lang="ts">
    import {fly} from "svelte/transition";
    import {description, type TDescription} from "./clickgui_store";

    let data: TDescription | null = null;

    description.subscribe((v) => {
        data = v;
    });

    let element: HTMLElement | null = null;
    let left = 0;
    let anchor: "right" | "left" = "right";

    $: {
        if (data?.x !== undefined && element !== null) {
            anchor = data.anchor;
            if (data.anchor === "left") {
                left = data.x - element.clientWidth - 20;
            } else {
                left = data.x + 20;
            }
        }
    }
</script>

{#key data}
    {#if data !== null}
        <div transition:fly|global={{duration: 200, x: anchor === "right" ? -15 : 15}} class="description-wrapper"
             style="top: {data.y}px; left: {left}px;" bind:this={element}>
            <div class="description" class:right={anchor === "left"}>
                <div class="text">{data.description}</div>
            </div>
        </div>
    {/if}
{/key}

<style lang="scss">
  @use "../../colors.scss" as *;

  .description-wrapper {
    position: fixed;
    z-index: 999999999999;
    transform: translateY(-50%);
  }

  /* Esports：0 圆角 + 霓虹描边与辉光（替代原来的柔和投影） */
  .description {
    position: relative;
    background-color: rgba($panel-color, .95);
    border: 1px solid $neon-color;
    box-shadow: 0 0 22px $neon-glow-sm;

    &::before {
      content: "";
      display: block;
      position: absolute;
      width: 0;
      height: 0;
      border-top: 8px solid transparent;
      border-bottom: 8px solid transparent;
      border-right: 8px solid $neon-color;
      left: -9px;
      top: 50%;
      transform: translateY(-50%);
    }

    &.right {
      &::before {
        transform: translateY(-50%) rotate(180deg);
        left: unset;
        right: -9px;
      }
    }
  }

  .text {
    font-family: var(--font-display);
    font-size: 12px;
    letter-spacing: 1px;
    padding: 10px;
    color: $clickgui-text-color;
  }

  /* Esports-FONT-INJECT */
  .text {
    font-family: var(--font-display);
  }
</style>