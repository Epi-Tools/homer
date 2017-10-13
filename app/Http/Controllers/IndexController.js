'use strict'

const Projects = use('App/Model/Project')

class IndexController {

    * index (request, response) {
        const projects = yield Projects.all()
        yield response.sendView('home', { projects: projects.toJSON()
            .sort((prev, next) => prev.spices - next.spices).reverse().slice(0, 5) })
    }

}

module.exports = IndexController
