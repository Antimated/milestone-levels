# Changelog

## v1.1.1
- Added migration to copy old settings to new settings

## v1.1.0
- Added XP milestones
- Added a "version manager" which displays a chatbox message with plugin update information (heavily inspired by [how @Zoinkwiz' does this in the quest helper](https://github.com/Zoinkwiz/quest-helper/blob/master/src/main/java/com/questhelper/managers/NewVersionManager.java))
- Refactored code to make XP milestones possible

## v1.0.6
- Added the Sailing skill
- Removed deprecation warning

## v1.0.5
- Fixed an issue where notifications were showing up after hopping from a leagues world to a main world.

## v1.0.4
- Fixed an issue where notifications got cleared when a loading screen would occur.

## v1.0.3
- Removed setting to enable multi-levels, this is now default behaviour.
- Added support for virtual levels

## v1.0.2
- Added multi-level support; leveling from level 1 to 20 at once will now trigger notifications for level 10 and 20 (if those are set in your levels list).
- Notifications now check if the notification interface is not opened before displaying it.
- General cleanup of code
- Added changelog

## v1.0.1
- Fixed issue where notifications would break after logout.
- Fixed issue where notifications would trigger on non-standard worlds.

## v1.0.0
- Initial release