<script lang="ts">
    import {onMount} from "svelte";
    import {
        getModuleSettings,
        setModuleSettings,
        setModuleEnabled,
    } from "../../integration/rest";
    import type {ConfigurableSetting} from "../../integration/types";
    import GenericSetting from "./setting/common/GenericSetting.svelte";
    import {slide} from "svelte/transition";
    import {quintOut} from "svelte/easing";
    import {description as descriptionStore, highlightModuleName} from "./clickgui_store";
    import {setItem} from "../../integration/persistent_storage";
    import {convertToSpacedString, spaceSeperatedNames} from "../../theme/theme_config";
    import {scaleFactor} from "./clickgui_store";

    export let name: string;
    export let enabled: boolean;
    export let description: string;
    export let aliases: string[];

    let moduleNameElement: HTMLElement;
    let configurable: ConfigurableSetting;
    const path = `clickgui.${name}`;
    let expanded = false;
    let hasSettings = false;

    onMount(async () => {
        await fetchModuleSettings();

        setTimeout(() => {
            expanded = localStorage.getItem(path) === "true"
        }, 500);
    });

    highlightModuleName.subscribe((m) => {
        if (name !== m) {
            return;
        }

        setTimeout(() => {
            if (!moduleNameElement) {
                return;
            }
            moduleNameElement.scrollIntoView({
                behavior: "smooth",
                block: "center",
            });
        }, 1000);
    });

    async function fetchModuleSettings() {
        configurable = await getModuleSettings(name);
        hasSettings = configurable.value.filter(v => v.name !== "Bind" && v.name !== "Hidden").length > 0;
    }

    async function updateModuleSettings() {
        await setModuleSettings(name, configurable);
        await fetchModuleSettings();
    }

    async function toggleModule() {
        await setModuleEnabled(name, !enabled);
    }

    function setDescription() {
        if (!moduleNameElement) return;

        const boundingRect = moduleNameElement.getBoundingClientRect();
        const y = (boundingRect.top + (moduleNameElement.clientHeight / 2)) * (2 / $scaleFactor);

        let moduleDescription = description;
        if (aliases.length > 0) {
            moduleDescription += ` (aka ${aliases.map(name => $spaceSeperatedNames ? convertToSpacedString(name) : name).join(", ")})`;
        }

        // If element is less than 300px from the right, display description on the left
        if (window.innerWidth - boundingRect.right > 300) {
            const x = boundingRect.right * (2 / $scaleFactor);
            descriptionStore.set({
                x,
                y,
                anchor: "right",
                description: moduleDescription
            });
        } else {
            const x = boundingRect.left * (2 / $scaleFactor);

            descriptionStore.set({
                x,
                y,
                anchor: "left",
                description: moduleDescription
            });
        }
    }

    async function toggleExpanded() {
        expanded = !expanded;
        await setItem(path, expanded.toString());
    }
</script>

<!-- svelte-ignore a11y-no-static-element-interactions -->
<div
        class="module"
        class:expanded
        class:has-settings={hasSettings}
        in:slide={{ duration: 500, easing: quintOut }}
        out:slide={{ duration: 500, easing: quintOut }}
>
    <!-- svelte-ignore a11y-click-events-have-key-events -->
    <div
            class="name"
            on:contextmenu|preventDefault={toggleExpanded}
            on:click={toggleModule}
            on:mouseenter={setDescription}
            on:mouseleave={() => descriptionStore.set(null)}
            bind:this={moduleNameElement}
            class:enabled
            class:highlight={name === $highlightModuleName}
    >
        {$spaceSeperatedNames ? convertToSpacedString(name) : name}
    </div>

    {#if expanded && configurable}
        <div class="settings">
            {#each configurable.value as setting (setting.name)}
                <GenericSetting {path} bind:setting on:change={updateModuleSettings}/>
            {/each}
        </div>
    {/if}
</div>

<style lang="scss">
  @use "../../colors" as *;
  @use "./icon-settings-expand" as *;

  /* Esports：0 圆角；启用态用左侧霓虹竖条 + 淡霓虹底表示，不用圆角高亮块。
     模块名由后端翻译返回，中文走系统字体回退（Rajdhani 无中文字形），
     故这里用 --font-display 让拉丁名拿到 Rajdhani、中文自动回退。 */
  .module {
    position: relative;

    .name {
      cursor: pointer;
      transition: background-color .14s ease, color .14s ease, border-color .14s ease;

      font-family: var(--font-display);
      color: $clickgui-text-dimmed-color;
      text-align: left;
      font-size: 13px;
      font-weight: 600;
      letter-spacing: 1px;
      position: relative;
      padding: 9px 10px 9px 12px;
      border-left: 2px solid transparent;
      font-variant-numeric: tabular-nums;

      /* 搜索命中：改为霓虹描边方块，0 圆角 */
      &.highlight::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        border: solid 2px $neon-color;
        box-shadow: 0 0 14px $neon-glow-sm;
        pointer-events: none;
      }

      &:hover {
        background-color: $neon-faint;
        color: $clickgui-text-color;
        border-left-color: $line-color-strong;
      }

      /* 启用态：霓虹文字 + 左竖条 + 辉光 */
      &.enabled {
        color: $neon-color;
        background-color: $neon-faint;
        border-left-color: $neon-color;
        text-shadow: 0 0 10px $neon-glow-sm;

        &:hover {
          background-color: rgba(240, 80, 140, 0.18);
        }
      }
    }

    .settings {
      background-color: rgba($panel-color, 0.92);
      border-left: solid 2px $neon-color;
      padding: 4px 10px 6px 10px;
    }

    &.has-settings {
      .name::after {
        @include icon-settings-expand($right: 15px);
        opacity: 0.5;
      }

      &.expanded .name::after {
        transform: translateY(-50%) rotate(0);
        opacity: 1;
      }
    }
  }

  /* Esports-FONT-INJECT */
  .name {
    font-family: var(--font-display);
  }
</style>
