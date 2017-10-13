'use strict'

/*
|--------------------------------------------------------------------------
| Router
|--------------------------------------------------------------------------
|
| AdonisJs Router helps you in defining urls and their actions. It supports
| all major HTTP conventions to keep your routes file descriptive and
| clean.
|
| @example
| Route.get('/user', 'UserController.index')
| Route.post('/user', 'UserController.store')
| Route.resource('user', 'UserController')
*/

const Route = use('Route')

Route.get('/', 'IndexController.index')

Route.get('/projects', 'ProjectsController.index')
Route.get('/projects/create', 'ProjectsController.create')
Route.post('/projects/create', 'ProjectsController.store')
Route.get('/projects/:id', 'ProjectsController.show')

Route.get('/profile', 'ProfileController.index')

Route.get('/about', 'AboutController.index')