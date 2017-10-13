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

    // TODO: fix date consistency
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
            spices: 'required|max:3|min:2',
            followUp1: 'required',
            followUp2: 'required',
            delivery: 'required',
            members: 'required',
            dateFollowUp1: 'required|date',
            dateFollowUp2: 'required|date',
            dateDelivery: 'required|date'
        }
        const validation = yield Validator.validate(data, rules) 
        const spicesNb = parseInt(data.spices)
        if (validation.fails() || spicesNb === NaN || spicesNb < 60 || spicesNb > 840) {
            const errors = validation.messages().length !== 0 ? validation.messages() : [ { message: 'Spices must be a number between 60 and 840' } ]   
            yield request.withOnly(...only).andWith({ errors })
                .flash() 
            response.redirect('back')
            return
        }
        data.spices = spicesNb
        yield Projects.create(data) 
        response.redirect('/projects')
    }

}

module.exports = ProjectsController
