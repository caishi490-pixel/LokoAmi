<script lang="ts">
    import {listen} from "../../../../integration/ws";
    import type {KeyEvent} from "../../../../integration/events";
    import type {MinecraftKeybind} from "../../../../integration/types";

    export let gridArea: string;
    export let key: MinecraftKeybind | undefined;

    let active = false;

    listen("key", (e: KeyEvent) => {
        if (e.key !== key?.key.translationKey) {
            return;
        }

        active = e.action === 1 || e.action === 2;
    });
</script>

<div class="key" style="grid-area: {gridArea};" class:active>
    {key?.key.localized ?? "???"}
</div>

<style lang="scss">
  @use "../../../../colors.scss" as *;

  /* Esports：0 圆角 + 霓虹描边；按下时整块填霓虹并反白 */
  .key {
    height: 50px;
    background-color: rgba($panel-color, 0.90);
    border: 1px solid $neon-dim;
    color: $keystrokes-text-color;
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: var(--font-display);
    font-size: 14px;
    font-weight: 700;
    letter-spacing: 1px;
    transition: ease box-shadow .2s, ease color .2s, ease border-color .2s;
    position: relative;
    box-shadow: inset 0 0 0 0 $neon-color;
    text-align: center;

    &.active {
      box-shadow: inset 0 0 0 50px $neon-color;
      color: $void-color;
      border-color: $neon-color;
    }
  }
</style>
