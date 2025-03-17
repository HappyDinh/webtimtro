package com.haui.web_demo.Service;

import com.haui.web_demo.entities.CustomerUserDetails;
import com.haui.web_demo.entities.User_Role;
import com.haui.web_demo.entities.Userobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Userobject userobject = userService.findByPhone(phone);
        if(userobject == null){
            throw new UsernameNotFoundException("Không tìm thấy");
        }
        Collection<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        Set<User_Role> roles = userobject.getUserRoles();
        for (User_Role account_Role : roles) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority(account_Role.getRole().getName()));
        }
        return new CustomerUserDetails(userobject, grantedAuthoritySet);
    }
}
