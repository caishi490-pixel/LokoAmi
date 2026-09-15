<script lang="ts">
    import {onMount} from "svelte";
    import type {GroupedModules, Module as TModule,} from "../../../../integration/types";
    import {getModules, setModuleEnabled} from "../../../../integration/rest";
    import {groupByCategory} from "../../../../integration/util";
    import Category from "./Category.svelte";
    import {getTextWidth} from "../../../../integration/text_measurement";
    import {listen} from "../../../../integration/ws";
    import {fly} from "svelte/transition";
    import Module from "./Module.svelte";
    import type {KeyEvent, ModuleToggleEvent} from "../../../../integration/events";

    let modules: TModule[] = [];
    let groupedModules: GroupedModules = {};
    let categories: string[] = [];
    let selectedCategoryIndex = 0;
    let selectedModuleIndex = 0;
    let renderedModules: TModule[] = [];
    let categoriesElement: HTMLElement;

    onMount(async () => {
        modules = (await getModules()).filter((m) => m.category !== "Client");
        groupedModules = groupByCategory(modules);
        categories = Object.keys(groupedModules).sort(
            (a, b) =>
                getTextWidth(b, "Inter 14px") - getTextWidth(a, "Inter 14px"),
        );
    });

    async function handleKeyDown(e: KeyEvent) {
        if (e.action !== 1) {
            return;
        }

        switch (e.key) {
            case "key.keyboard.down":
                if (renderedModules.length === 0) {
                    selectedCategoryIndex =
                        (selectedCategoryIndex + 1) %
                        Object.keys(categories).length;
                } else {
                    selectedModuleIndex =
                        (selectedModuleIndex + 1) % renderedModules.length;
                }
                break;
            case "key.keyboard.up":
                if (renderedModules.length === 0) {
                    selectedCategoryIndex =
                        (selectedCategoryIndex -
                            1 +
                            Object.keys(categories).length) %
                        Object.keys(categories).length;
                } else {
                    selectedModuleIndex =
                        (selectedModuleIndex - 1 + renderedModules.length) % renderedModules.length;
                }
                break;
            case "key.keyboard.left":
                renderedModules = [];
                selectedModuleIndex = 0;
                break;
            case "key.keyboard.right":
                if (renderedModules.length > 0) {
                    return;
                }
                renderedModules =
                    groupedModules[categories[selectedCategoryIndex]];
                break;
            case "key.keyboard.enter":
                if (renderedModules.length === 0) {
                    return;
                }
                const mod = renderedModules[selectedModuleIndex];
                await setModuleEnabled(mod.name, !mod.enabled);
                break;
        }
    }

    listen("key", handleKeyDown);

    listen("moduleToggle", (e: ModuleToggleEvent) => {
        const moduleName = e.moduleName;
        const moduleEnabled = e.enabled;

        const mod = modules.find((m) => m.name === moduleName);
        if (!mod) return;

        mod.enabled = moduleEnabled;
        groupedModules = groupByCategory(modules);
        if (renderedModules.length > 0) {
            renderedModules = groupedModules[categories[selectedCategoryIndex]];
        }
    });
</script>

<div class="tabgui">
    <div class="categories" bind:this={categoriesElement}>
        {#each categories as name, index}
            <Category {name} selected={index === selectedCategoryIndex} />
        {/each}
    </div>

    {#if renderedModules.length > 0}
        <div class="modules" transition:fly={{ x: -10, duration: 200 }} style="height: {categoriesElement.offsetHeight}px">
            {#each renderedModules as { name, enabled }, index}
                <Module {name} {enabled} selected={selectedModuleIndex === index} />
            {/each}
        </div>
    {/if}
</div>

<style lang="scss">
    @use "../../../../colors.scss" as *;

    /* Esports：整块面板 0 圆角 + 右上切角 + 霓虹描边 */
    .tabgui {
        display: flex;
        background-color: rgba($panel-color, 0.88);
        border: 1px solid $neon-dim;
        clip-path: polygon(0 0, calc(100% - 10px) 0, 100% 10px, 100% 100%, 0 100%);
    }

    .categories {
        display: flex;
        flex-direction: column;
        overflow: hidden;
    }

    /* 展开的模块列表：以霓虹竖条与分类栏分隔 */
    .modules {
      background-color: rgba($void-color, 0.9);
      margin-left: 1px;
      border-left: 2px solid $neon-color;
      min-width: 100px;
      display: flex;
      flex-direction: column;
      overflow: auto;

      &::-webkit-scrollbar {
        width: 0;
      }
    }
</style>
