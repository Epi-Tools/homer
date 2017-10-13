'use strict'

const Projects = use('App/Model/Project')
const Validator = use('Validator')

class ProjectsController {

    * index (request, response) {
        const projects = yield Projects.all()
        yield response.sendView('projects', { projects: projects.toJSON() })
    }

    * create (request, response) {
        yield response.sendView('projects.create')
    }

    * store (request, response) {
        const only = [ 'name', 'description', 'github', 'presentation',
            'spices', 'followUp1', 'followUp2', 'delivery', 'members',
            'dateFollowUp1', 'dateFollowUp2', 'dateDelivery' ]
        const data = request.only(...only)
        const rules = {
            name: 'required|max:255',
            description: 'required',
            github: 'url',
            presentation: 'required|url',
            spices: 'required',
            followUp1: 'required',
            followUp2: 'required',
            delivery: 'required',
            members: 'required',
            dateFollowUp1: 'required|date',
            dateFollowUp2: 'required|date',
            dateDelivery: 'required|date'
        }
        const validation = yield Validator.validate(data, rules) 
        if (validation.fails()) {
            yield request.withOnly(...only).andWith({ errors: validation.messages() })
            .flash() 
            response.redirect('back')
            return
        }
        yield Post.create(postData) 
        response.redirect('/projects')
    }

}

module.exports = ProjectsController
