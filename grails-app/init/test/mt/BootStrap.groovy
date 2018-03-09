package test.mt

import grails.gorm.multitenancy.Tenants
import grails.gorm.multitenancy.WithoutTenant
import grails.gorm.transactions.Transactional

class BootStrap {

    def init = { servletContext ->
        create()
        listAllPeople()
    }

    @WithoutTenant
    @Transactional
    def listAllPeople() {
        Person.findAll().each {
            println "$it.name $it.tenantId"
        }
    }

    @Transactional
    def create() {
        def tenant = new Tenant(name:"tenant1").save(failOnError:true)
        def tenantId = tenant.id
        Tenants.withId(tenantId) {
            new Person(name:"me1", tenantId: tenantId).save()
        }

        def tenant2 = new Tenant(name:"tenant2").save(failOnError:true)
        def tenant2Id = tenant2.id
        Tenants.withId(tenant2Id) {
            new Person(name:"me2").save()
        }
    }


    def destroy = {
    }
}
