/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.mixin.core.entity.living;

import static org.spongepowered.api.data.DataQuery.of;

import net.minecraft.entity.EntityAgeable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.Ageable;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@NonnullByDefault
@Mixin(EntityAgeable.class)
@Implements(@Interface(iface = Ageable.class, prefix = "ageable$"))
public abstract class MixinEntityAgeable extends MixinEntityLiving {

    @Shadow public abstract int getGrowingAge();
    @Shadow public abstract void setGrowingAge(int age);
    @Shadow public abstract void setScaleForAge(boolean baby);

    public int getAge() {
        return getGrowingAge();
    }

    public void setAge(int age) {
        setGrowingAge(age);
    }

    public void setBaby() {
        if (getGrowingAge() >= 0) {
            setGrowingAge(-24000);
        }
    }

    public void setAdult() {
        if (getGrowingAge() < 0) {
            setGrowingAge(0);
        }
    }

    public boolean isBaby() {
        return getGrowingAge() < 0;
    }

    public boolean canBreed() {
        return getGrowingAge() == 0;
    }

    public void setBreeding(boolean breeding) {
        if (breeding) {
            setGrowingAge(0);
        } else if (getGrowingAge() >= 0) {
            setGrowingAge(6000);
        }
    }

    public void setScaleForAge() {
        setScaleForAge(getGrowingAge() < 0);
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = super.toContainer();
        container.set(of("GrowthAge"), this.getAge());
        return container;
    }
}
