'use strict'

class ProjectsController {

    * index (request, response) {
        yield response.sendView('projects')
    }
    
}

module.exports = ProjectsController
