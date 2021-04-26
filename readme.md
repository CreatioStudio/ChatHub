# Chat Bridge

> Bungeecord 跨服聊天插件

在 Bungeecord 服务器中跨服聊天

![screenshot1](screenshot1.jpg)

## 功能

- Bungeecord 内各子服聊天互通
- 可配置的直达服务器消息前缀
- 可配置的加入、离开、切换服务器、消息事件格式
- 可配置的服务器ID和自定义名称转换
- 多 BC 间互通广播事件
- 不依赖子服核心，纯 Bungeecord 插件

## 配置文件

配置文件为 `plugins/ChatBridge/config.yml`

## ignore_prefix

默认值：`!!`

忽略处理的聊天消息前缀

## message

默认值：

```yaml
join: '§8[§a+§8] §e{player}'
leave: '§8[§c-§8] §e{player}'
switch: '§8[§b❖§8] §e{player}§r: §7«{serverFrom}§7» §6➟ §7«{serverTo}§7»'
chat: '§7[{serverOn}§7]§e{player}§r: {message}'
```

不同事件显示的消息，`{variable}` 将被替换

## server_name

默认值：

```yaml
serverID: serverName
```

服务器ID和服务器名的对照表

### broadcast

多 BC 间广播消息配置

#### port

默认值：`51800`

接收广播消息的端口

#### token

默认值：`token`

秘钥

#### servers

默认值：

```yaml
- 'host:port'
```

广播的目标服务器
