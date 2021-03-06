package ru.pavlik.chempred.client.model.js;

import com.github.gwtd3.api.layout.Force;
import ru.pavlik.chempred.client.model.LinkType;

public class ElementLink extends Force.Link<ElementLink> {

    protected ElementLink() {
    }

    public static native ElementLink create(ElementNode source, ElementNode target, LinkType linkType) /*-{
        return {
            source: source,
            target: target,
            linkType: linkType
        }
    }-*/;

    public final native LinkType getType() /*-{
        return this.linkType;
    }-*/;
}
