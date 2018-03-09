import test.mt.UserDetailsService
import test.mt.UserPasswordEncoderListener
// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(UserDetailsService)
}
