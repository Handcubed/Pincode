package xyz.amayakasa.pincode.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.Configuration;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Pincode message registry.
 */
public class PincodeMessageRegistry {

    private final MiniMessage parser = MiniMessage.miniMessage();
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final Configuration configuration;


    public PincodeMessageRegistry(Configuration configuration) {
        this.configuration = configuration;
    }


    private String getRandomRawMessage(List<String> rawMessages) {
        final var size = rawMessages.size();
        final var randomIndex = random.nextInt(size);

        return rawMessages.get(randomIndex);
    }

    private String getMessage(String key) {
        final var messages = configuration.getStringList(key);

        if (messages.isEmpty()) throw new IllegalStateException("Messages cannot be empty!");

        return getRandomRawMessage(messages);
    }

    private String getAltMessage(String key) {
        final var message = configuration.getString(key);

        if (message == null) throw new IllegalStateException("Message cannot be null!");

        return message;
    }

    public Component getPincodeCreateSuggestionMessage() {
        final var message = parser.deserialize(getMessage("pincodeCreateSuggestion"));
        final var altMessage = parser.deserialize(getAltMessage("altPincodeCreateSuggestion"));

        return message.hoverEvent(HoverEvent.showText(altMessage));
    }

    public Component getPincodeCreateConfirmationMessage(String pincode) {
        var pincodePlaceholder = Placeholder.component("pincode", Component.text(pincode));

        final var message = parser.deserialize(getMessage("pincodeCreateConfirmation"), pincodePlaceholder);
        final var altMessage = parser.deserialize(getAltMessage("altPincodeCreateConfirmation"), pincodePlaceholder);

        return message.hoverEvent(HoverEvent.showText(altMessage));
    }

    public Component getPincodeInputSuggestionMessage() {
        final var message = parser.deserialize(getMessage("pincodeInputSuggestion"));
        final var altMessage = parser.deserialize(getAltMessage("altPincodeInputSuggestion"));

        return message.hoverEvent(HoverEvent.showText(altMessage));
    }

    public Component getPincodeInputConfirmationMessage() {
        final var message = parser.deserialize(getMessage("pincodeInputConfirmation"));
        final var altMessage = parser.deserialize(getAltMessage("altPincodeInputConfirmation"));

        return message.hoverEvent(HoverEvent.showText(altMessage));
    }

    public Component getInvalidPincodeMessage() {
        final var message = parser.deserialize(getMessage("invalidPincode"));
        final var altMessage = parser.deserialize(getAltMessage("altInvalidPincode"));

        return message.hoverEvent(HoverEvent.showText(altMessage));
    }

    public Component getWrongPincodeMessage() {
        final var message = parser.deserialize(getMessage("wrongPincode"));
        final var altMessage = parser.deserialize(getAltMessage("altWrongPincode"));

        return message.hoverEvent(HoverEvent.showText(altMessage));
    }
}
