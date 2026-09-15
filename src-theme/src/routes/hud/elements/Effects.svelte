<script lang="ts">
    import {listen} from "../../../integration/ws";
    import type {ClientPlayerDataEvent, ClientPlayerEffectEvent} from "../../../integration/events";
    import type {StatusEffect} from "../../../integration/types";
    import {effectTextureUrl} from "../../../integration/rest";

    let effects: StatusEffect[] = [];

    listen("clientPlayerData", (event: ClientPlayerDataEvent) => {
        effects = event.playerData.effects;
    });

    listen("clientPlayerEffect", (event: ClientPlayerEffectEvent) => {
        effects = event.effects;
    });

    function formatTime(duration: number): string {
        if (duration === -1) {
            return "*:*";
        }

        const totalSeconds = Math.floor(duration / 20);
        const minutes = Math.floor(totalSeconds / 60);
        const seconds = totalSeconds % 60;

        return `${minutes}:${seconds.toString().padStart(2, "0")}`;
    }

    function formatAmplifier(n: number): string {
        return (n + 1).toString();
    }
</script>

{#if effects.length > 0}
    <div class="effects">
        {#each effects as e}
            <div class="effect">
                <img class="effect-icon" src={effectTextureUrl(e.effect)} alt={e.localizedName}/>
                <span class="name">{e.localizedName}  <span
                        class="amplifier">{formatAmplifier(e.amplifier)}</span></span>
                <span class="duration">{formatTime(e.duration)}</span>
            </div>
        {/each}
    </div>
{/if}

<style lang="scss">
  @use "../../../colors.scss" as *;

  /* Esports：0 圆角 + 右上切角 + 霓虹描边；药水时长用等宽数字 */
  .effects {
    display: flex;
    flex-direction: column;
    gap: 4px;
    background-color: $effects-background-color;
    border: 1px solid $neon-dim;
    clip-path: polygon(0 0, calc(100% - 10px) 0, 100% 10px, 100% 100%, 0 100%);
    box-shadow: 0 0 18px $neon-glow-sm;
    padding: 5px 8px;
  }

  .effect {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 500;
    font-size: 14px;

    .effect-icon {
      width: 16px;
      height: 16px;
      image-rendering: pixelated;
      image-rendering: -moz-crisp-edges;
      image-rendering: crisp-edges;
    }

    .name {
      font-family: var(--font-display);
      font-weight: 600;
      letter-spacing: 1px;
      color: $effects-name-color;
    }

    .amplifier {
      color: $effects-amplifier-color;
      font-weight: 700;
    }

    .duration {
      margin-left: auto;
      font-family: monospace;
      color: $effects-duration-color;
      font-size: 12px;
      font-variant-numeric: tabular-nums;
    }
  }
</style>
