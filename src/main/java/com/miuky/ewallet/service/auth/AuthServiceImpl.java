package com.miuky.ewallet.service.auth;

import com.miuky.ewallet.dto.auth.request.LoginRequest;
import com.miuky.ewallet.dto.auth.request.RegisterRequest;
import com.miuky.ewallet.dto.auth.response.AuthResponse;
import com.miuky.ewallet.entity.User;
import com.miuky.ewallet.entity.Wallet;
import com.miuky.ewallet.exception.AppException;
import com.miuky.ewallet.exception.ErrorCode;
import com.miuky.ewallet.repository.UserRepository;
import com.miuky.ewallet.repository.WalletRepository;
import com.miuky.ewallet.security.CustomUserDetails;
import com.miuky.ewallet.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Slf4j
public class AuthServiceImpl implements IAuthService{
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final WalletRepository walletRepo;

    @Override @Transactional(rollbackFor = Exception.class)
    public AuthResponse register(RegisterRequest request) {
        if (userRepo.existsByPhoneNumber(request.phoneNumber())) throw new AppException(ErrorCode.PHONE_EXISTED);

        User newUser = User.builder()
                .phoneNumber(request.phoneNumber())
                .passwordHash(encoder.encode(request.password()))
                .fullName(request.fullName())
                .build();
        User savedUser = userRepo.save(newUser);

        Wallet newWallet = Wallet.builder().user(savedUser).build();
        walletRepo.save(newWallet);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, savedUser.getId(), savedUser.getFullName());
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        try {
            log.info("HERE");
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.phoneNumber(), request.password()));
            log.info("HERE1");
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User currUser = userDetails.getUser();
            String token = jwtService.generateToken(userDetails);

            return new AuthResponse(token, currUser.getId(), currUser.getFullName());
        } catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);

        } catch (LockedException e) {
            throw new AppException(ErrorCode.WALLET_LOCKED);

        } catch (DisabledException e) {
            throw new AppException(ErrorCode.USER_BANNED);
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            throw new AppException(ErrorCode.SUCCESS);
        }
    }
}
