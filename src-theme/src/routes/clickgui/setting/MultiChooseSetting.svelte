<script lang="ts">
    import {createEventDispatcher} from "svelte";
    import type {ModuleSetting, MultiChooseSetting,} from "../../../integration/types";
    import {slide} from "svelte/transition";
    import {convertToSpacedString, spaceSeperatedNames} from "../../../theme/theme_config";
    import ExpandArrow from "./common/ExpandArrow.svelte";
    import {setItem} from "../../../integration/persistent_storage";

    export let setting: ModuleSetting;
    export let path: string;

    const cSetting = setting as MultiChooseSetting;
    const thisPath = `${path}.${cSetting.name}`;

    let errorValue: string | null = null;
    let timeoutId: ReturnType<typeof setTimeout>;

    const dispatch = createEventDispatcher();

    function handleChange(v: string) {
        if (cSetting.value.includes(v)) {
            const filtered = cSetting.value.filter(item => item !== v);

            if (filtered.length === 0 && !cSetting.canBeNone) {
                // Doesn't remove the element because in this case the value will be empty
                // And indicate the value
                errorValue = v
                clearTimeout(timeoutId);
                timeoutId = setTimeout(() => errorValue = null, 300);

                return;
            }

            cSetting.value = filtered;
        } else {
            cSetting.value = [...cSetting.value, v]
        }

        setting = {...cSetting};
        dispatch("change");
    }

    let expanded = localStorage.getItem(thisPath) === "true";

    $: setItem(thisPath, expanded.toString());

    function toggleExpanded() {
        expanded = !expanded;
    }
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<!-- svelte-ignore a11y-no-static-element-interactions -->
<div class="setting">
    <div class="head" class:expanded on:contextmenu|preventDefault={toggleExpanded}>
        <div class="title">{$spaceSeperatedNames ? convertToSpacedString(cSetting.name) : cSetting.name}</div>
        <div class="amount">{cSetting.value.length}/{cSetting.choices.length}</div>
        <ExpandArrow bind:expanded/>
    </div>

    {#if expanded}
        <div class="choices" transition:slide|global={{duration: 200, axis: "y"}}>
            {#each cSetting.choices as choice (choice)}
                <div
                        class="choice"
                        class:active={cSetting.value.includes(choice)}
                        class:error={errorValue === choice}
                        on:click={() => {
                            handleChange(choice)
                        }}
                >
                    {$spaceSeperatedNames ? convertToSpacedString(choice) : choice}
                </div>
            {/each}
        </div>
    {/if}
</div>

<style lang="scss">
  @use "../../../colors.scss" as *;

  .setting {
    padding: 7px 0;
    color: $clickgui-text-color;
  }

  .title {
    color: $clickgui-text-color;
    font-size: 12px;
    font-weight: 600;
  }

  .choice {
    color: $clickgui-text-dimmed-color;
    background-color: rgba($panel-color, 0.3);
    border-radius: 0;
    padding: 3px 6px;
    cursor: pointer;
    font-weight: 500;
    transition: ease color 0.2s;
    overflow-wrap: anywhere;

    &:hover {
      color: $clickgui-text-color;
    }

    &.error {
      background-color: rgba($menu-error-color, 0.1) !important;
      color: $menu-error-color !important;
    }

    &.active {
      background-color: rgba($neon-color, 0.1);
      color: $neon-color;
    }
  }

  .amount {
    letter-spacing: 1px;
    font-weight: 500;
    font-size: 12px;
    font-family: monospace;
  }

  .head {
    display: grid;
    grid-template-columns: 1fr max-content max-content;
    transition: ease margin-bottom .2s;
    align-items: center;

    &.expanded {
      margin-bottom: 10px;
    }
  }

  .choices {
    border-left: solid 2px $neon-color;
    color: $clickgui-text-color;
    padding: 7px 7px;
    display: flex;
    flex-wrap: wrap;
    gap: 7px;
    font-size: 12px;
  }

  /* Esports：统一字体栈与等宽数字（补完注入） */
  .title {
    font-family: var(--font-display);
  }

  /* Esports-FONT-INJECT */
  .title {
    font-family: var(--font-display);
  }
</style>
