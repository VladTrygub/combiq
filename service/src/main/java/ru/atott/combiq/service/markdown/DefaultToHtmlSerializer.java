package ru.atott.combiq.service.markdown;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import ru.atott.combiq.service.site.UserContext;
import ru.atott.combiq.service.user.UserRoles;

public class DefaultToHtmlSerializer extends ToHtmlSerializer {

    private UserContext uc;

    public DefaultToHtmlSerializer(UserContext uc) {
        this(uc, new LinkRenderer());
    }

    public DefaultToHtmlSerializer(UserContext uc, LinkRenderer linkRenderer) {
        super(linkRenderer);
        this.uc = uc;
    }

    protected boolean isUserAllowedToPrintImageTag() {
        return uc != null && uc.hasAnyRole(UserRoles.sa, UserRoles.contenter);
    }

    @Override
    protected void printImageTag(LinkRenderer.Rendering rendering) {
        if (isUserAllowedToPrintImageTag()) {
            super.printImageTag(rendering);
        }
    }
}
