<script lang="ts">
    import {listen} from "../../../integration/ws";
    import type {PlayerData, Scoreboard} from "../../../integration/types";
    import TextComponent from "../../menu/common/TextComponent.svelte";
    import type {ClientPlayerDataEvent} from "../../../integration/events";

    export let settings: { [name: string]: any };

    const cSettings = settings as HudScoreboardSettings;

    let scoreboard: Scoreboard | null = null;

    listen("clientPlayerData", (e: ClientPlayerDataEvent) => {
        const playerData: PlayerData = e.playerData;
        scoreboard = playerData.scoreboard;
    });
</script>

{#if scoreboard}
    <div class="scoreboard">
        {#if scoreboard.header && cSettings.show.includes('Header')}
            <div class="header">
                <TextComponent fontSize={14} allowPreformatting={true} textComponent={scoreboard.header}/>
            </div>
        {/if}
        <div class="entries">
            {#each scoreboard.entries as {name, score}}
                <div class="row">
                    {#if cSettings.show.includes('Name')}
                        <TextComponent fontSize={14} allowPreformatting={true} textComponent={name}/>
                    {/if}
                    {#if cSettings.show.includes('Score')}
                        <TextComponent fontSize={14} allowPreformatting={true} textComponent={score}/>
                    {/if}
                </div>
            {/each}
        </div>
    </div>
{/if}

<style lang="scss">
  @use "../../../colors.scss" as *;

  /* Esports：0 圆角 + 右上切角 + 霓虹描边；数字等宽对齐 */
  .scoreboard {
    width: max-content;
    overflow: hidden;
    font-size: 14px;
    font-family: var(--font-display);
    border: 1px solid $neon-dim;
    clip-path: polygon(0 0, calc(100% - 10px) 0, 100% 10px, 100% 100%, 0 100%);
    box-shadow: 0 0 20px $neon-glow-sm;
  }

  .entries {
    background-color: rgba($panel-color, 0.86);
    padding: 10px;
    font-variant-numeric: tabular-nums;
    letter-spacing: 1px;
  }

  .row {
    display: flex;
    column-gap: 15px;
    justify-content: space-between;
  }

  .header {
    text-align: center;
    background-color: $neon-color;
    color: $void-color;
    font-weight: 700;
    letter-spacing: 2px;
    padding: 6px 10px;
  }
</style>
