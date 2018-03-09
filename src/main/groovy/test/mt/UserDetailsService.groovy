package test.mt

import grails.gorm.multitenancy.Tenants
import grails.gorm.multitenancy.WithoutTenant
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailsService implements GrailsUserDetailsService {

    /**
     * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least
     * one role, so we give a user with no granted roles this one which gets
     * past that restriction but doesn't grant anything.
     */
    static final List NO_ROLES = [new SimpleGrantedAuthority(SpringSecurityUtils.NO_ROLE)]

    UserDetails loadUserByUsername(String username, boolean loadRoles)
            throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }

    @WithoutTenant
    @Transactional(readOnly=true, noRollbackFor=[IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = Person.createCriteria().get {
                eq('username', username, [ignoreCase: true])
            }
        if (!person) {
            throw new NoStackUsernameNotFoundException()
        }

        def roles = person.authorities

        // or if you are using role groups:
        // def roles = user.authorities.collect { it.authorities }.flatten().unique()

        def authorities = roles.collect {
            new SimpleGrantedAuthority(it.authority)
        }

        new GrailsUser(person.username, person.password, person.enabled, !person.accountExpired, !person.passwordExpired,
                !person.accountLocked, authorities, person.id)

    }
}