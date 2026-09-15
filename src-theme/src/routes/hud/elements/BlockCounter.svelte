<script lang="ts">
    import {listen} from "../../../integration/ws";
    import {fly} from "svelte/transition";
    import {mapToColor} from "../../../util/color_utils";
    import {itemTextureUrl} from "../../../integration/rest";

    export let settings: { [name: string]: any };

    const cSettings = settings as HudBlockCounterSettings;

    let nextBlock: string | undefined = undefined;
    let count: number | undefined = undefined;

    listen("blockCountChange", (data) => {
        nextBlock = data.nextBlock;
        count = data.count;
    });

    const FLEX_DIRECTION = {
        None: "row",
        Left: "row",
        Right: "row-reverse",
        Top: "column",
        Bottom: "column-reverse",
    };
</script>

{#if count !== undefined}
    <div class="counter" style="color: {mapToColor(count)}; flex-direction: {FLEX_DIRECTION[cSettings.iconPosition]}" in:fly={{ y: -5, duration: 200 }}
         out:fly={{ y: -5, duration: 200 }}>
        {#if nextBlock && cSettings.iconPosition !== "None"}
            <img class="icon" src={itemTextureUrl(nextBlock)} alt={nextBlock}/>
        {/if}
        {count}
    </div>
{/if}

<style lang="scss">
  @use "../../../colors.scss" as *;

  /* Esports：0 圆角 + 切角 + 霓虹描边；数量用等宽数字 */
  .counter {
    background-color: rgba($panel-color, 0.92);
    border: 1px solid $neon-dim;
    clip-path: polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 0 100%);
    box-shadow: 0 0 16px $neon-glow-sm;
    white-space: nowrap;
    padding: 5px 9px;
    font-family: var(--font-display);
    font-weight: 700;
    font-variant-numeric: tabular-nums;
    letter-spacing: 1px;
    text-align: center;
    width: fit-content;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
    transform: translate(-100%);
  }

  .icon {
    width: 24px;
    height: 24px;
  }
</style>
