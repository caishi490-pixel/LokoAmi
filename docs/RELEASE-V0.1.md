# LokoAmi V0.1 发布声明

> **Minecraft 1.21.11 · Fabric · GNU GPL-3.0**

|  |  |
| :--- | :--- |
| **版本** | V0.1 |
| **作者** | lie_fish（管弦彩石） |
| **反馈** | caishi490@gmail.com |
| **源码** | https://github.com/caishi490-pixel/LokoAmi |
| **基底** | LiquidBounce 0.37.0（CCBlueX） |
| **许可** | GNU General Public License v3.0 |

---

## 一、关于 LokoAmi

LokoAmi 是一个基于 Fabric 的 Minecraft 客户端，由 **LiquidBounce 0.37.0** 二次开发而来。

原版 LiquidBounce 功能庞杂，且整体偏向 PVP 对抗场景，其中相当一部分模块并不适合日常游玩与角色扮演环境。LokoAmi 想做的是一次**做减法**：拆掉高风险的对抗类模块，保留真正顺手的操作辅助，再把整个视觉与交互重做一遍。

标识是一枚**左倾 45° 的樱花瓣**，配色是**深海蓝黑 + 玫粉**。

---

## 二、V0.1 更新内容

### 1. 品牌与标识全面重做

- 资源命名空间由 `liquidbounce` 统一迁移至 `lokoami`
- 客户端显示名、模组 ID、配置目录、主题资源包全部同步更名
- 全新标识：左倾 45° 樱花瓣，粉 → 玫红渐变，简洁无内部线条
- 启动页署名区：

  > **贡献清单 · 开发 lie_fish**
  > **版本号 V0.1**

### 2. 视觉配色

废弃原版蓝色，整体换成深海蓝黑打底 + 玫粉点缀：

| 用途 | 色值 |
| :--- | :--- |
| 主强调色 | `#F0508C` 玫粉 |
| 界面底色 | `#0A1628` 深海蓝黑 |
| 主文字 | `#EAF2FF` |
| 次级文字 | `#8FA6C4` |

ClickGUI、TabGUI、ArrayList、TargetHUD 全部同步。

### 3. 新增功能

- **无限夜视** —— 无需手持物品，全天候视野清晰
- **自动疾跑** —— 移动即自动进入疾跑状态

### 4. 自定义模块框架

新增独立的 **Custom** 分类，内置 6 个模块位置，后续按需填充：

```
FishGuard · SimmcChannel · AdminActionLog
ArcaneRange · EntitySizeNormalize · PacketCapture
```

> V0.1 中这些模块为**空壳占位**，暂不具备功能。

---

## 三、模块调整说明

本版移除了原客户端中风险最高的对抗类模块，mixin 注入点由 **158 项精简至 138 项**，并清理了 **473 处悬空引用**（覆盖 Kotlin、Java mixin、accesswidener 与资源文件），确保拆除后不会出现缺失符号导致的崩溃。

**需要明确说明的是：** LokoAmi 依然源自 LiquidBounce，**它不是"没有作弊功能的客户端"**。请在你所游玩服务器的规则允许范围内使用。

---

## 四、安装步骤

1. 确保已安装 **Fabric Loader**（建议 ≥ 0.19.x）
2. 确保游戏版本为 **Minecraft 1.21.11**
3. 下载 `LokoAmi-0.1.jar`
4. 放入 `.minecraft/mods/` 目录
5. 确认以下**两个前置模组**同样位于 `mods/` 中：
   - Fabric API
   - Fabric Language Kotlin
6. 启动游戏

> ⚠️ **注意**：由于客户端名已变更，首次启动会生成**全新的配置目录 `LokoAmi\`**，原 `LiquidBounce\` 目录下的旧配置不会自动迁移。如需保留，请手动复制。

---

## 五、兼容性

| 项目 | 要求 |
| :--- | :--- |
| Minecraft | 1.21.11 |
| Fabric Loader | ≥ 0.19.x |
| Fabric API | 0.141.6+1.21.11 |
| Fabric Language Kotlin | 1.13.13+kotlin.2.4.10 |
| Java | 21 |

---

## 六、开源与许可

LokoAmi 是 **LiquidBounce 0.37.0** 的衍生作品，遵循 **GNU General Public License v3.0**。

这意味着：

- ✅ 你可以自由使用、修改、再分发本项目
- ⚠️ 再分发时（**无论源码还是编译后的 jar**）**必须同样以 GPL-3.0 授权**，并向接收者提供完整对应源码
- ⚠️ **必须保留**原作者的版权声明与许可证文本
- ❌ 本作品**不提供任何形式的担保**

|  |  |
| :--- | :--- |
| **源码仓库** | https://github.com/caishi490-pixel/LokoAmi |
| **上游项目** | https://github.com/CCBlueX/LiquidBounce |

---

## 七、已知问题

- 启动页仅完成标识与配色替换，**版式仍沿用原版布局**
- 6 个自定义模块为**空壳**，暂无功能
- ClickGUI / HUD 仅有配色改动，**未做结构级重构**
- V0.1 尚未经过大规模实机验证，可能存在问题

---

## 八、下一步计划

- 启动页版式重做，与樱花瓣标识统一风格
- 逐个实现 6 个自定义模块
- ClickGUI / HUD 结构级重构
- 收集反馈并修复 V0.1 暴露的问题

---

## 九、致谢

- **CCBlueX** 与 **LiquidBounce** 全体贡献者 —— 没有上游就没有这个项目
- **Fabric** 与 **Fabric Language Kotlin** 团队
- 所有参与测试与反馈的朋友

---

## 十、免责声明

1. 本项目为**非官方**作品，与 **Mojang Studios**、**Microsoft** 及 **CCBlueX** 均无隶属或合作关系。
2. 本项目**按"现状"提供**，不附带任何明示或暗示的担保。
3. 请遵守你所游玩服务器的规则与当地法律法规。**因使用本项目产生的一切后果由使用者自行承担，作者不承担任何责任。**
4. 请勿将本项目用于商业用途后再以闭源方式分发 —— 这违反 GPL-3.0。

---

*Made with 🐋 & 🌸 by lie_fish*
