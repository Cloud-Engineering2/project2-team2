package com.recipe.cookofking.config;


//@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true)
//@EnableWebSecurity
//@Configuration
public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
//
//        security
//                .csrf(csrf -> csrf.disable());
//        //.cors(cors -> cors.disable());
//        security.authorizeHttpRequests(authorizeRequests ->
//                authorizeRequests
//                        .requestMatchers("/assets/**").permitAll() // Allow static assets
//                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                        .requestMatchers("/u/details/**").hasRole("USER")
//                        .requestMatchers("/f/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/login", "/register", "/", "/f/c", "/f", "/f/a", "/f/a/{artist_id}").permitAll()
//                        .anyRequest().authenticated()
//        );
//        security.formLogin(form -> form
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .usernameParameter("email") // 사용자 이름 필드를 이메일로 설정
//                .passwordParameter("password") // 비밀번호 필드 설정
//                .defaultSuccessUrl("/f/c")); //f/c
//
//        security.logout(logout -> logout.logoutSuccessUrl("/f/c"));
//        //세션
//        security.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
//                .invalidSessionUrl("/login")
//                .sessionFixation().migrateSession() //로그인후 세션 변경
//                .maximumSessions(1)//한개만 유지
//                .expiredUrl("/login"));//만료시 이동
//
//        return security.build();
//
//    }
//
//
//    @Bean
//    public UserDetailsService userDetailsService(UserService userService) {
//        return email -> userService.searchUserByEmail(email).map(FanUserDetails::toFUDto)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//    }
//
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//
//       return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
//    }

}
