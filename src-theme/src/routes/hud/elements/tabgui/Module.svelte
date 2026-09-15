<script lang="ts">
    import {afterUpdate} from "svelte";
    import {setModuleEnabled} from "../../../../integration/rest";
    import {convertToSpacedString, spaceSeperatedNames} from "../../../../theme/theme_config";

    export let name: string;
    export let enabled: boolean;
    export let selected: boolean;

    let moduleElement: HTMLElement;

    afterUpdate(() => {
        if (moduleElement && selected) {
            moduleElement.scrollIntoView({
                behavior: "smooth",
                block: "nearest",
            });
        }
    });

    async function handleKeyDown(e: KeyboardEvent) {
        if (selected && e.key === "Enter") {
            await setModuleEnabled(name, !enabled);
        }
    }
</script>

<svelte:window on:keydown={handleKeyDown} />

<div class="module" class:enabled class:selected bind:this={moduleElement}>
    <div class="name">{$spaceSeperatedNames ? convertToSpacedString(name) : name}</div>
</div>

<style lang="scss">
    @use "../../../../colors.scss" as *;

    /* Esports：选中用霓虹左竖条 + 淡霓虹底；启用态文字转霓虹 */
    .module {
        font-family: var(--font-display);
        font-weight: 600;
        color: $tabgui-text-dimmed-color;
        font-size: 12.5px;
        letter-spacing: 1px;
        padding: 6px 15px 6px 10px;
        border-left: 2px solid transparent;
        transition: color .14s ease, background-color .14s ease, border-color .14s ease;

        .name {
            transition: transform .2s ease;
        }

        &.selected {
            background-color: $neon-faint;
            border-left-color: $neon-color;

            .name {
                transform: translateX(5px);
            }
        }

        &.enabled {
            color: $neon-color;
            text-shadow: 0 0 10px $neon-glow-sm;
        }
    }
</style>
