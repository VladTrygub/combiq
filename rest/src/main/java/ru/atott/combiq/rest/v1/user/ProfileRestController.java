package ru.atott.combiq.rest.v1.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.atott.combiq.rest.request.ProfileEmailRequest;
import ru.atott.combiq.rest.v1.BaseRestController;
import ru.atott.combiq.service.site.UserContext;
import ru.atott.combiq.service.user.ProfileService;

import javax.validation.Valid;
import java.util.Objects;

@RestController
public class ProfileRestController extends BaseRestController {

    @Autowired
    private ProfileService profileService;

    /**
     * Изменить email в профиле пользователя.
     *
     * @request.body.example
     *  {@link ru.atott.combiq.rest.request.ProfileEmailRequest#EXAMPLE}
     *
     * @param userId
     *      Идентификатор пользователя, чей профиль меняется.
     *
     * @response.200.doc
     *      В случае успеха.
     */
    @RequestMapping(value = "/rest/v1/user/{userId}/profile/email", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user')")
    public Object applyProfileEmail(
            @PathVariable("userId") String userId,
            @Valid @RequestBody ProfileEmailRequest request) {

        UserContext uc = getContext().getUc();

        if (!Objects.equals(uc.getUserId(), userId)) {
            return responseForbidden();
        }

        profileService.applyProfileEmail(uc, request.getEmail());

        return responseOk();
    }
}
