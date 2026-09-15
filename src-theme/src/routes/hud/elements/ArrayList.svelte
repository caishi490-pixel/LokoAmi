<script lang="ts">
    import {onMount, tick} from "svelte";
    import type {Module} from "../../../integration/types";
    import {getModules} from "../../../integration/rest";
    import {listen} from "../../../integration/ws";
    import {getTextWidth} from "../../../integration/text_measurement";
    import {flip} from "svelte/animate";
    import {fly} from "svelte/transition";
    import {convertToSpacedString, spaceSeperatedNames} from "../../../theme/theme_config";

    export let settings: { [name: string]: any };

    const cSettings = settings as HudArrayListSettings;

    let enabledModules: Module[] = [];

    async function updateEnabledModules() {
        const modules = await getModules();
        const visibleModules = modules.filter(m => m.enabled && !m.hidden);

        const modulesWithWidths = visibleModules.map(module => {
            const formattedName = $spaceSeperatedNames ? convertToSpacedString(module.name) : module.name;
            const fullName = module.tag == null || !cSettings.showTags
                ? formattedName
                : formattedName + " " + module.tag;

            return {
                ...module,
                width: getTextWidth(fullName, "500 14px Inter")
            };
        });

        modulesWithWidths.sort((a, b) => cSettings.order === "Ascending" ? a.width - b.width : b.width - a.width);

        enabledModules = modulesWithWidths;
        await tick();
    }

    spaceSeperatedNames.subscribe(async () => {
        await updateEnabledModules();
    });

    onMount(async () => {
        await updateEnabledModules();
    });

    listen("moduleToggle", async () => {
        await updateEnabledModules();
    });

    listen("refreshArrayList", async () => {
        await updateEnabledModules();
    });
</script>

<div class="arraylist">
    {#each enabledModules as {name, tag} (name)}
        <div
                class="module"
                style={cSettings.itemAlignment === "Left" ? "margin-right: auto;" : "margin-left: auto;"}
                animate:flip={{ duration: 200 }}
                transition:fly={{ x: 50, duration: 200 }}
        >
            {$spaceSeperatedNames ? convertToSpacedString(name) : name}
            {#if tag && cSettings.showTags}
                <span class="tag"> {tag}</span>
            {/if}
        </div>
    {/each}
</div>

<style lang="scss">
  @use "../../../colors.scss" as *;

  /* Esports：0 圆角 + 右上切角 + 左侧霓虹状态条。
     模块名多为中文，用 --font-display 让拉丁名拿到 Rajdhani、中文自动回退。
     数字走等宽，模块计数／延迟跳动时不抖。 */
  .module {
    background-color: rgba($panel-color, 0.82);
    color: $arraylist-text-color;
    font-family: var(--font-display);
    font-size: 14px;
    font-weight: 600;
    letter-spacing: 1px;
    font-variant-numeric: tabular-nums;
    padding: 4px 11px 4px 9px;
    border-left: solid 3px $neon-color;
    width: max-content;
    clip-path: polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 0 100%);
    box-shadow: -8px 0 14px -8px $neon-glow;
  }

  .tag {
    color: $arraylist-tag-color;
    font-size: 12px;
    font-weight: 500;
  }
</style>
