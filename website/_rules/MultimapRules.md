---
title: MultimapRules
name: MultimapRules
severity: SUGGESTION
tags:
- Simplification
source: error-prone-contrib/src/main/java/tech/picnic/errorprone/refasterrules/MultimapRules.java
rules:
- name: MultimapKeySet
  severity: SUGGESTION
  tags:
  - Simplification
  diff: |2-
     Set<String> testMultimapKeySet() {
    -    return ImmutableSetMultimap.of("foo", "bar").asMap().keySet();
    +    return ImmutableSetMultimap.of("foo", "bar").keySet();
       }
- name: MultimapSize
  severity: SUGGESTION
  tags:
  - Simplification
  diff: |2-
     int testMultimapSize() {
    -    return ImmutableSetMultimap.of().values().size();
    +    return ImmutableSetMultimap.of().size();
       }
- name: MultimapGet
  severity: SUGGESTION
  tags:
  - Simplification
  diff: |2-
     ImmutableSet<Collection<Integer>> testMultimapGet() {
         return ImmutableSet.of(
    -        ImmutableSetMultimap.of(1, 2).asMap().get(1),
    -        Multimaps.asMap((Multimap<Integer, Integer>) ImmutableSetMultimap.of(1, 2)).get(1));
    +        ImmutableSetMultimap.of(1, 2).get(1),
    +        ((Multimap<Integer, Integer>) ImmutableSetMultimap.of(1, 2)).get(1));
       }
---
