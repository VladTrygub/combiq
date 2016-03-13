package ru.atott.combiq.service.markdown;

import org.pegdown.LinkRenderer;
import org.pegdown.ast.*;
import ru.atott.combiq.service.site.UserContext;

public class ToSimplifiedHtmlSerializer extends DefaultToHtmlSerializer {

    public ToSimplifiedHtmlSerializer(UserContext uc) {
        super(uc);
    }

    @Override
    protected void printLink(LinkRenderer.Rendering rendering) {
        printer.print(rendering.text);
    }

    @Override
    protected void printImageTag(LinkRenderer.Rendering rendering) {
        if (isUserAllowedToPrintImageTag()) {
            printer.print("[Изображение]");
        }
    }

    @Override
    public void visit(BlockQuoteNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(BulletListNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(CodeNode node) {
        printer.printEncoded(node.getText());
    }

    @Override
    public void visit(DefinitionListNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(DefinitionNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(DefinitionTermNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(HeaderNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(HtmlBlockNode node) {
        printer.print("[HTML блок]");
    }

    @Override
    public void visit(StrongEmphSuperNode node) {
        if(node.isClosed()){
            visitChildren(node);
        } else {
            printer.print(node.getChars());
            visitChildren(node);
        }
    }

    @Override
    public void visit(ListItemNode node) {
        printer.println();
        visitChildren(node);
    }

    @Override
    public void visit(OrderedListNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(ParaNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableBodyNode node) {
        // It's not supported.
    }

    @Override
    public void visit(TableCaptionNode node) {
        // It's not supported.
    }

    @Override
    public void visit(TableCellNode node) {
        // It's not supported.
    }

    @Override
    public void visit(TableColumnNode node) {
        // It's not supported.
    }

    @Override
    public void visit(TableHeaderNode node) {
        // It's not supported.
    }

    @Override
    public void visit(TableNode node) {
        // It's not supported.
    }

    @Override
    public void visit(TableRowNode node) {
        // It's not supported.
    }
}
