package ru.atott.combiq.service.dsl;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Terminals;

import java.util.List;
import java.util.stream.Collectors;

public class DslParser {

    private static Parser<DslQuery> parser;

    public static DslQuery parse(String dsl) {
        if (StringUtils.isBlank(dsl)) {
            return new DslQuery();
        }
        dsl = dsl.trim();
        if (parser == null) {
            parser = getDslQueryParser();
        }
        return parser.parse(dsl);
    }

    private static Parser<DslQuery> getDslQueryParser() {
        Parser<Void> ignored = Parsers.never();

        Terminals operators = Terminals.operators("[", "]", " ", ":");
        Parser<String> singleQuoteTokenizer = Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER;
        Parser<?> wordTokenizer = Scanners.notAmong("[]: ").many().source();
        Parser<?> tokenizer = Parsers.or(operators.tokenizer(), singleQuoteTokenizer, wordTokenizer);

        // ---

        Parser<String> wordParser = Parsers.tokenType(String.class, "string");

        // key:value
        Parser<DslPair> pairParser = wordParser
                .followedBy(operators.token(":").times(1))
                .next(key -> wordParser.map(value -> new DslPair(key, value)));

        // [tag]
        Parser<DslTag> tagParser = wordParser
                .between(operators.token("["), operators.token("]"))
                .map(DslTag::new);

        // term
        Parser<DslTerm> termParser = wordParser
                .map(DslTerm::new);

        Parser<Object> conditionsParser = Parsers.or(pairParser, tagParser, termParser);

        Parser<DslQuery> queryParser = conditionsParser
                .sepBy(operators.token(" ").many())
                .map(conditions -> {
                    List<DslTag> tags = conditions.stream()
                            .filter(condition -> condition instanceof DslTag)
                            .map(DslTag.class::cast)
                            .collect(Collectors.toList());

                    List<DslTerm> terms = conditions.stream()
                            .filter(condition -> condition instanceof DslTerm)
                            .map(DslTerm.class::cast)
                            .collect(Collectors.toList());

                    DslQuery query = new DslQuery();
                    query.setTags(tags);
                    query.setTerms(terms);

                    List<DslPair> pairs = conditions.stream()
                            .filter(condition -> condition instanceof DslPair)
                            .map(DslPair.class::cast)
                            .collect(Collectors.toList());

                    pairs.forEach(pair -> {
                        switch (pair.getKey()) {
                            case "level":
                                query.setLevel(pair.getValue());
                                break;
                            case "comments":
                                try {
                                    query.setMinCommentQuantity(Long.valueOf(pair.getValue()));
                                } catch (Exception e) {
                                    // Nothing to do.
                                }
                                break;
                            case "deleted":
                                query.setDeleted(BooleanUtils.toBoolean(pair.getValue()));
                                break;
                            case "user":
                                if (StringUtils.isNotBlank(pair.getValue())) {
                                    query.setUser(pair.getValue());
                                }
                                break;
                            case "favorite":
                                query.setFavorite(BooleanUtils.toBoolean(pair.getValue()));
                                break;
                        }
                    });

                    return query;
                });

        return queryParser.from(tokenizer, ignored.skipMany());
    }
}
