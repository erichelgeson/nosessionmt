package test.mt

import grails.gorm.MultiTenant

class Person implements MultiTenant<Person> {

    Long tenantId
    String name

    static constraints = {
    }
}
