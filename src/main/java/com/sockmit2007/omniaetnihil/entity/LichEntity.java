package com.sockmit2007.omniaetnihil.entity;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableRangedAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.ConditionlessAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

public class LichEntity extends PathfinderMob implements Enemy, GeoEntity, SmartBrainOwner<LichEntity>, RangedAttackMob {
    public LichEntity(EntityType<? extends LichEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controllerName", 0, event -> {
            return event.setAndContinue(event.isMoving() ? RawAnimation.begin().thenLoop("idle") : RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getWeaponItem() != null && pSource.getWeaponItem().isEnchanted() && (pSource.getWeaponItem().getEnchantmentLevel(level().holderOrThrow(Enchantments.SHARPNESS)) > 0 || pSource.getWeaponItem().getEnchantmentLevel(level().holderOrThrow(Enchantments.SMITE)) > 0 || pSource.getWeaponItem().getEnchantmentLevel(level().holderOrThrow(Enchantments.BANE_OF_ARTHROPODS)) > 0)) {
            return !super.hurt(pSource, pAmount);
        }

        return super.hurt(pSource, pAmount);
    }

    // .setParticleKeyframeHandler(event -> {if (this.level().isClientSide) {if (event.getKeyframeData().getEffect().matches("arcane_effect")) {OmniaEtNihil.LOGGER.info(event.getKeyframeData().getLocator());// this.level().addParticle(OmniaEtNihil.ARCANE_PARTICLE, );// this.level().addParticle(null, lerpX, lerpY, lerpZ, XP_REWARD_BOSS, LEASH_TOO_FAR_DIST, LEASH_ELASTIC_DIST);}}})

    public static AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 125.0).add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.ATTACK_SPEED, 2.4).add(Attributes.FOLLOW_RANGE, 50.0).add(Attributes.MOVEMENT_SPEED, 0.4);
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    public List<ExtendedSensor<? extends LichEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(), // This tracks nearby entities
                new HurtBySensor<>()); // This tracks the last damage source and attacker

    }

    @Override
    public BrainActivityGroup<? extends LichEntity> getCoreTasks() { // These are the tasks that run all the time
                                                                     // (usually)
        return BrainActivityGroup.coreTasks(new LookAtTarget<>(), // Have the entity turn to face and look at its current look target
                new MoveToWalkTarget<>()); // Walk towards the current walk target
    }

    @SuppressWarnings("unchecked") @Override
    public BrainActivityGroup<? extends LichEntity> getIdleTasks() { // These are the tasks that run when the mob isn't doing anything else (usually)
        return BrainActivityGroup.idleTasks(new FirstApplicableBehaviour<LichEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>(), new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.5f), new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<? extends LichEntity> getFightTasks() { // These are the tasks that handle fighting
        return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>(), // Cancel fighting if the target is no longer valid
                new SetWalkTargetToAttackTarget<>(), // Set the walk target to the attack target
                new AnimatableRangedAttack<>(0).whenStarting(entity -> setAggressive(true)).whenStopping(entity -> setAggressive(false)), // Ranged attack the target if close enough
                new AnimatableMeleeAttack<>(0).whenStarting(entity -> setAggressive(true)).whenStopping(entity -> setAggressive(false)), new ConditionlessAttack<>(20).requiresTarget().attack(entity -> {
                    LivingEntity target = BrainUtils.getTargetOfEntity(entity);

                    if (target != null) {
                        target.push(target.position().vectorTo(entity.position()).multiply(new Vec3(1, 0, 1)).normalize().multiply(25, 0, 25));
                    }
                }).cooldownFor((entity) -> 120).startCondition((entity) -> {
                    LivingEntity target = BrainUtils.getTargetOfEntity(entity);

                    return target != null && target.distanceToSqr(entity) > 15;
                }));
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        this.getPosition(0);
    }
}
