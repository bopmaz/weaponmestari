# Weapon mestari
Small project to showcase room and architecture stuff

This small project is created to demonstrate ability to use Room database along with ability to architect and refactor database if needed

About architecture:
- The app based on Repository pattern, which use local database (Room) as the single source of truth.
- The presentation layer is built upon very simple MVI pattern
- The app ultilized 3 different kind of model: `Local model` for the presentation layer, `Remote model` for the networking and `Database model` for database
- Other tools used include Hilt for Dependency Injection, Retrofit for networking (with an Interceptor to temporary mock the response from backend), new lifecycle to manage lifecycle of the activity, ViewBinding to replace `findViewByIds`

How the app works:
- When start, app check for local database, if the database is not exist, it will fetch from network, after which it will be saved to local database, before being offered to presentation layer.

For the migration of the database:
- The app added a cross-reference table to handle the new relationship between warrior and weapon, which you can check with the pull request for issue #1

For the handle of edit weapon:
- Check commit https://github.com/bopmaz/weaponmestari/pull/1/commits/2c44880ddb9d65988b31c11a6d294664dd97522a

Preview of the app (Fresh start no data -> have local data)
![](weapon_mestari.gif)
