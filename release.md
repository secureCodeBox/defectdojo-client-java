# Release Documentation

Here we describe all the ceremonial stuff necessary to publish a Java library to Maven Central.

## GPG Guide for Maven Release Signing

This guide is based on [Working with PGP Signatures](https://central.sonatype.org/publish/requirements/gpg/) and [OpenPGP Best Practices](https://riseup.net/ru/security/message-security/openpgp/gpg-best-practices).

### About our key

Real name: `the secureCodeBox authors`
Email: `securecodebox@iteratec.com`
Comment: `Maven Release Signing Key`
Fingerprint: `40AA7D29EB6DE0667D7E723ADE4725604A739BAF`
Password: [see our password manager]

### Create a new key

We create a new GPG key with:

```shell
gpg --full-generate-key
```

### Import the private key

Download private key from 1Password and import it locally

```shell
gpg --import private.key
```

### Export the Private Key

#### For GitHub Actions

```shell
gpg --armor --export-secret-keys 40AA7D29EB6DE0667D7E723ADE4725604A739BAF
```

#### For 1Password

```shell
gpg -o private.key --export-secret-key 40AA7D29EB6DE0667D7E723ADE4725604A739BAF
```

### Expiration

It is recommended to use an expiration date less than two years. We use an interval of **two years**. This means that we need to extend the expiration date every two years! 

#### How to extend the expiration date?

1. Download the private key file `private.key` from 1Password
2. Import it locally:  `gpg --import private.key`
3. Select the key : `gpg --edit-key 40AA7D29EB6DE0667D7E723ADE4725604A739BAF`
4. Now select the subkey and set the expire date (use `2y` for two years):
```shell
gpg> key 1
gpg> expire
```
5. Save it:
```shell
gpg>  save
```
6. Update the private key in out password manager and GitHub Secrets

## TODOs

- How to remember the expirationd date over time & with changing developers?
- Do we need to import the public key in sonatype?
