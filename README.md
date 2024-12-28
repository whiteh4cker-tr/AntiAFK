# AntiAFK
 This plugin detects water circles, jump AFK, and more.

![GitHub License](https://img.shields.io/github/license/whiteh4cker-tr/AntiAFK?style=flat)
[![CodeFactor](https://www.codefactor.io/repository/github/whiteh4cker-tr/antiafk/badge)](https://www.codefactor.io/repository/github/whiteh4cker-tr/antiafk)

## Features

-   Water loop detection
-   Jump AFK detection

-   Experimental clicker detection (not tested)
-   Kick AFK players

-   Customizable kick messages

![alt text](https://i.imgur.com/4bQM2zl.gif)

## Command - Description - Permission
**/antiafk reload** - Reload the Plugin - **antiafk.admin**

## Permissions
antiafk.bypass - Bypasses the AFK Checks

## Config
```
max-afk-seconds: 300
max-clicks-per-second: 10
enable-water-loop-detection: true
enable-auto-clicker-detection: true
kick-message-afk: "&cYou have been kicked for being AFK."
kick-message-water-loop: "&cYou have been kicked for using a water loop."
kick-message-auto-clicker: "&cYou have been kicked for using an auto-clicker."
auto-clicker-time-threshold: 300 # 5 minutes
auto-clicker-warning-interval: 30 # 30 seconds
water-loop-movement-threshold: 20
water-loop-time-threshold: 300 # 5 minutes
```

## Requirements

-   Java 21 or higher
-   Spigot/Paper/forks MC v1.21.4
