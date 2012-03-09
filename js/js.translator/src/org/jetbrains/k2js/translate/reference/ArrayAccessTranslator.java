/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.k2js.translate.reference;

import com.google.common.collect.Lists;
import com.google.dart.compiler.backend.js.ast.JsExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.FunctionDescriptor;
import org.jetbrains.jet.lang.psi.JetArrayAccessExpression;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.general.AbstractTranslator;
import org.jetbrains.k2js.translate.general.Translation;
import org.jetbrains.k2js.translate.utils.TranslationUtils;

import java.util.List;

import static org.jetbrains.k2js.translate.utils.BindingUtils.getDescriptorForReferenceExpression;
import static org.jetbrains.k2js.translate.utils.BindingUtils.getResolvedCall;

/**
 * @author Pavel Talanov
 */

//TODO: inspect not clear how the class handles set and get operations differently
public class ArrayAccessTranslator extends AbstractTranslator implements AccessTranslator {

    /*package*/
    static ArrayAccessTranslator newInstance(@NotNull JetArrayAccessExpression expression,
                                             @NotNull TranslationContext context) {
        return new ArrayAccessTranslator(expression, context);
    }

    @NotNull
    private final JetArrayAccessExpression expression;
    @NotNull
    private final FunctionDescriptor methodDescriptor;

    protected ArrayAccessTranslator(@NotNull JetArrayAccessExpression expression,
                                    @NotNull TranslationContext context) {
        super(context);
        this.expression = expression;
        //TODO: that is strange
        this.methodDescriptor = (FunctionDescriptor)
                getDescriptorForReferenceExpression(context.bindingContext(), expression);
    }

    @NotNull
    @Override
    public JsExpression translateAsGet() {
        return translateAsGet(translateArrayExpression(), translateIndexExpressions());
    }

    @NotNull
    protected JsExpression translateAsGet(@NotNull JsExpression arrayExpression,
                                          @NotNull List<JsExpression> indexExpression) {
        return translateAsMethodCall(arrayExpression, indexExpression);
    }

    @NotNull
    @Override
    public JsExpression translateAsSet(@NotNull JsExpression setTo) {
        return translateAsSet(translateArrayExpression(), translateIndexExpressions(), setTo);
    }

    @NotNull
    protected JsExpression translateAsSet(@NotNull JsExpression arrayExpression, @NotNull List<JsExpression> indexExpressions, @NotNull JsExpression toSetTo) {
        List<JsExpression> arguments = Lists.newArrayList(indexExpressions);
        arguments.add(toSetTo);
        return translateAsMethodCall(arrayExpression, arguments);
    }

    @NotNull
    private JsExpression translateAsMethodCall(@NotNull JsExpression arrayExpression, @NotNull List<JsExpression> arguments) {
        return CallBuilder.build(context())
                .receiver(arrayExpression)
                .args(arguments)
                .resolvedCall(getResolvedCall(bindingContext(), expression))
                .descriptor(methodDescriptor)
                .translate();
    }

    @NotNull
    protected List<JsExpression> translateIndexExpressions() {
        return TranslationUtils.translateExpressionList(context(), expression.getIndexExpressions());
    }

    @NotNull
    protected JsExpression translateArrayExpression() {
        return Translation.translateAsExpression(expression.getArrayExpression(), context());
    }

    @NotNull
    @Override
    public CachedAccessTranslator getCached() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
