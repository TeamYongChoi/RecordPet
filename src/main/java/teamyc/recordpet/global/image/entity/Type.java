package teamyc.recordpet.global.image.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Type {
    PET("PET", "반려동물"),
    USER("USER", "사용자");

    private final String code;
    private final String value;
}