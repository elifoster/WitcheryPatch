package io.github.elifoster.witcherypatch;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class Transformer implements IClassTransformer {
    static boolean isEnvObfuscated;

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (name.equals("com.emoniph.witchery.integration.NEIDistilleryRecipeHandler")) {
            ClassNode classNode = readClassFromBytes(bytes);
            MethodNode method = findMethodNodeOfClass(classNode, "loadCraftingRecipes", "(Lnet/minecraft/item/ItemStack;)V");
            /*
            Hooks.loadCraftingRecipes(this, result);
            return;
             */
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
            toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Hooks.class), "loadCraftingRecipes", "(Lcom/emoniph/witchery/integration/NEIDistilleryRecipeHandler;Lnet/minecraft/item/ItemStack;)V", false));
            toInject.add(new InsnNode(Opcodes.RETURN));

            method.instructions.insertBefore(findFirstInstruction(method), toInject);

            return writeClassToBytes(classNode);
        }
        return bytes;
    }

    // Shout out to squeek.
    private ClassNode readClassFromBytes(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        return classNode;
    }

    private byte[] writeClassToBytes(ClassNode classNode) {
        return writeClassToBytes(classNode, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }

    private byte[] writeClassToBytes(ClassNode classNode, int flags) {
        ClassWriter writer = new ClassWriter(flags);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private MethodNode findMethodNodeOfClass(ClassNode classNode, String methodName, String methodDesc) {
        for (MethodNode method : classNode.methods) {
            if (method.name.equals(methodName) && method.desc.equals(methodDesc)) {
                return method;
            }
        }
        return null;
    }

    private AbstractInsnNode getOrFindInstruction(AbstractInsnNode firstInsnToCheck) {
        return getOrFindInstruction(firstInsnToCheck, false);
    }

    private AbstractInsnNode getOrFindInstruction(AbstractInsnNode firstInsnToCheck, boolean reverseDirection) {
        for (AbstractInsnNode instruction = firstInsnToCheck; instruction != null; instruction = reverseDirection ? instruction.getPrevious() : instruction.getNext()) {
            if (instruction.getType() != AbstractInsnNode.LABEL && instruction.getType() != AbstractInsnNode.LINE) {
                return instruction;
            }
        }
        return null;
    }

    private AbstractInsnNode findFirstInstruction(MethodNode method) {
        return getOrFindInstruction(method.instructions.getFirst());
    }
}
