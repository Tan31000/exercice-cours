package fr.orsys.sec;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	 @Autowired
     private DataSource dataSource;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	/*	auth.inMemoryAuthentication().withUser("admin1").password("{noop}1234").roles("USER", "ADMIN");
		auth.inMemoryAuthentication().withUser("user1").password("{noop}1234").roles("USER");
		*/
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select username as principal , password, active as credentials from app_user where username=?")
		.authoritiesByUsernameQuery("select username as principal , role_name as role from app_user au, app_role ar, app_user_roles aur "
				+ "Where au.id=aur.app_user_id and ar.id=aur.roles_id and au.username=?")
		.passwordEncoder(bCryptPasswordEncoder)
		.rolePrefix("ROLE_");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login");
		http.authorizeRequests().antMatchers("/user/*").hasRole("USER");
		http.authorizeRequests().antMatchers("/admin/*").hasRole("ADMIN");
		http.exceptionHandling().accessDeniedPage("/403");
	}
}
