'use strict'

class IndexController {

    * index (request, response) {
        yield response.sendView('home')
    }

}

module.exports = IndexController
