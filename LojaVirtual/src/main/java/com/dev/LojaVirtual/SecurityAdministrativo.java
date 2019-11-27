package com.dev.LojaVirtual;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityAdministrativo extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;



	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery(
						"select email as username, senha as password, 1 as enable from funcionario where email=?")
				.authoritiesByUsernameQuery(
						"select funcionario.email as username, papel.nome as authority from permissoes_funcionario inner join funcionario on funcionario.id=permissoes_funcionario.funcionario_id inner join papel on permissoes_funcionario.papel_id=papel.id where funcionario.email=?")
				.passwordEncoder(new BCryptPasswordEncoder());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/administrativo/**").hasAnyAuthority("Vendedor","Gerente").
		antMatchers("/administrativo/estado/**").hasAuthority("gerente").and().formLogin()
				.loginPage("/login").permitAll()
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/administrativo").and()
				.exceptionHandling().accessDeniedPage("/negadoAdministrativo");

	}

}
