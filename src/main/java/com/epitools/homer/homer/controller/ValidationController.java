package com.epitools.homer.homer.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class ValidationController {
}
