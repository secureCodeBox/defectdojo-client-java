# Release Documentation

Here we describe all the ceremonial stuff necessary to publish a Java library to Maven Central.


## Credentials

Publishing to Maven Central requires authentication in the form of a username-password or an User Token (as username and password).
We use User Tokens [2] for authentication, which are stored in our password manager.

### Local (on device)

The `MAVEN_USERNAME` and `MAVEN_PASSWORD` environment variable needs to be set on the device.

### GitHub Actions

In the GitHub Secrets [1], we need to add two secrets called `MAVEN_USERNAME` and `MAVEN_PASSWORD` (Github enforces a leading `SECRET_TOKEN`).
They can be accessed in a yaml file with `${{ secrets.MAVEN_USERNAME }}` and `${{ secrets.MAVEN_PASSWORD }}`.
We pass both these secrets in the `env` block.

For example:

```yaml
- name: Publish to Maven Central
  run: ./gradlew publish
  env:
    MAVEN_USERNAME: ${{ secrets.MAVEN_USERNAME }}
    MAVEN_PASSWORD: ${{ secrets.MAVEN_PASSWORD }}
```

## GPG Guide for Maven Signing

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

Download private key from password manager and import it locally

```shell
gpg --import private.key
```

### Export the Private Key

#### For GitHub Actions

```shell
gpg --armor --export-secret-keys $KEYID
```

In the GitHub Secrets [1], add the output of this command to the `SIGNING_KEY` secret.
Additionally, you must add the corresponding password in as `SIGNING_PASSWORD`.
Both can be accessed in a yaml file with `${{ secrets.SIGNING_KEY }}` and `${{ secrets.SIGNING_PASSWORD }}`.
We pass both these secrets in the `env` block-

For example:

```yaml
- name: Publish to Maven Central
  run: ./gradlew publish
  env:
    SIGNING_KEY: ${{ secrets.SIGNING_KEY }}
    SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}
    ...
```

#### For Password Manager

```shell
gpg -o private.key --export-secret-key $KEYID
```

### Expiration

It is recommended to use an expiration date less than two years.
We use an interval of **two years**.
This means that we need to extend the expiration date every two years!
To remember, we added an appointment to the team calendar.

#### How to extend the expiration date?

1. Download the private key file `private.key` from password manager
2. Import it locally:  `gpg --import private.key`
3. Select the key : `gpg --edit-key $KEYID`
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

- Do we need to import the public key in sonatype?

[1]: https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/GitHub-Actions-Secrets-Example-Token-Tutorial
[2]: https://help.sonatype.com/iqserver/managing/user-management/user-tokens
