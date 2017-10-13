'use strict'

const Projects = use('App/Model/Project')

class ProjectsController {

    * index (request, response) {
        const projects = yield Projects.all()
        yield response.sendView('projects', { projects: projects.toJSON() })
    }

}

module.exports = ProjectsController
